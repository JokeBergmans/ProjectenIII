using damiaanapp_API.DTOs;
using damiaanapp_API.Models;
using damiaanapp_API.Models.Domain;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Identity;
using Microsoft.AspNetCore.Mvc;
using Microsoft.Extensions.Configuration;
using Microsoft.IdentityModel.Tokens;
using Microsoft.VisualBasic;
using Newtonsoft.Json.Linq;
using System;
using System.Collections.Generic;
using System.IdentityModel.Tokens.Jwt;
using System.Linq;
using System.Security.Claims;
using System.Text;
using System.Threading.Tasks;

namespace damiaanapp_API.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    [ApiConventionType(typeof(DefaultApiConventions))]
    public class AccountController : ControllerBase
    {
        private readonly SignInManager<IdentityUser> _signInManager;
        private readonly UserManager<IdentityUser> _userManager;
        private readonly IParticipantRepository _participantRepository;
        private readonly IConfiguration _config;

        public AccountController(
          SignInManager<IdentityUser> signInManager,
          UserManager<IdentityUser> userManager,
          IParticipantRepository participantRepository,
          IConfiguration config)
        {
            _signInManager = signInManager;
            _userManager = userManager;
            _participantRepository = participantRepository;
            _config = config;
        }

        #region Methods
        /*[HttpPost("addAdmin")]
        [AllowAnonymous]
        public async Task<ActionResult> AddAdminAsync()
        {

            Participant admin = new Participant("damiaanadmin", "damiaanadmin", "damiaanadmin@mail.com", DateAndTime.Now.AddYears(-99), "adminstraat", "1", "Gent", 9000, false);

            var user = new IdentityUser { UserName = "damiaanadmin", Email = "damiaanadmin@mail.com" };
            var result = await _userManager.CreateAsync(user, "P@ssword1111!");
            result = await _userManager.AddClaimAsync(user, new Claim(ClaimTypes.Role, "admin"));

            if (result.Succeeded)
            {
                _participantRepository.Add(admin);
                _participantRepository.SaveChanges();
                string token = await GetToken(user);
                Participant p = _participantRepository.GetByEmail(admin.Email);
                JObject rss = new JObject(
                    new JProperty("token", token),
                    new JProperty("id", p.Id)
                );
                return Created("", rss);
            }
            return Ok(result.Errors);
        }*/

        /// <summary>
        /// Login
        /// </summary>
        /// <param name="model">the login details</param>
        [AllowAnonymous]
        [HttpPost]
        public async Task<ActionResult<string>> CreateToken(LoginDTO model)
        {
            var user = await _userManager.FindByNameAsync(model.Email);
            if (user != null)
            {
                var result = await _signInManager.CheckPasswordSignInAsync(user, model.Password, false);

                if (result.Succeeded)
                {
                    string token = await GetToken(user);
                    Participant p = _participantRepository.GetByEmail(user.Email);
                    JObject rss = new JObject(
                        new JProperty("token", token),
                        new JProperty("id", p.Id)
                    );
                    return Created("", rss);
                    //return Created("", token); //returns only the token                    
                }
            }
            return BadRequest("Something went wrong trying to login");
        }

        /// <summary>
        /// Register a user
        /// </summary>
        /// <param name="model">the user details</param>
        /// <returns></returns>
        [AllowAnonymous]
        [HttpPost("Register")]
        public async Task<ActionResult<string>> Register(RegisterDTO model)
        {
            IdentityUser user = new IdentityUser { UserName = model.Email, Email = model.Email };
            Participant participant = new Participant { Email = model.Email, FirstName = model.FirstName, Name = model.Name, BirthDate = model.BirthDate, CanBeFollowed = true };
            var result = await _userManager.CreateAsync(user, model.Password);
            result = await _userManager.AddClaimAsync(user, new Claim(ClaimTypes.Role, "customer"));
            if (result.Succeeded)
            {
                _participantRepository.Add(participant);
                _participantRepository.SaveChanges();
                string token = await GetToken(user);
                Participant p = _participantRepository.GetByEmail(user.Email);
                JObject rss = new JObject(
                    new JProperty("token", token),
                    new JProperty("id", p.Id)
                );
                return Created("", rss);
                //return Created("", token);
            }
            return BadRequest("Something went wrong trying to register new user: " + result.Errors);
        }

        /// <summary>
        /// Checks if an email is available as username
        /// </summary>
        /// <returns>true if the email is not registered yet</returns>
        /// <param name="email">Email.</param>/
        [AllowAnonymous]
        [HttpGet("CheckUsername")]
        public async Task<ActionResult<bool>> CheckAvailableUserName(string email)
        {
            var user = await _userManager.FindByNameAsync(email);
            return user == null;
        }

        private async Task<string> GetToken(IdentityUser user)
        {
            // Create the token
            var claims = new List<Claim>
            {
              new Claim(JwtRegisteredClaimNames.Sub, user.Email),
              new Claim(JwtRegisteredClaimNames.UniqueName, user.UserName)
            };

            var roles = await _userManager.GetClaimsAsync(user);
            var role = roles.FirstOrDefault();
            if (role != null)
                claims.Add(new Claim("role", role.Value));


            //var key = new SymmetricSecurityKey(Encoding.UTF8.GetBytes(_config["Tokens:Key"]));
            var key = new SymmetricSecurityKey(Encoding.UTF8.GetBytes("voorlopigeTestkeyTotVaultWerkt"));

            var creds = new SigningCredentials(key, SecurityAlgorithms.HmacSha256);

            var token = new JwtSecurityToken(
              null, null,
              claims,
              expires: DateTime.Now.AddMinutes(30),
              signingCredentials: creds);

            return new JwtSecurityTokenHandler().WriteToken(token);
        }
        #endregion 
    }
}

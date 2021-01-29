using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using damiaanapp_API.Data;
using damiaanapp_API.DTOs;
using damiaanapp_API.Models;
using damiaanapp_API.Models.Domain;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;

namespace damiaanapp_API.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class ParticipantController : ControllerBase
    {
        private readonly ApplicationDbContext _context;
        private readonly IParticipantRepository _partcipantRepository;
        private readonly IRegistrationRepository _registrationRepository;
        public ParticipantController(IParticipantRepository participantRepository, IRegistrationRepository registrationRepository, ApplicationDbContext dbContext)
        {
            _partcipantRepository = participantRepository;
            _registrationRepository = registrationRepository;
            _context = dbContext;
        }


        [HttpGet]
        public IEnumerable<Participant> GetParticipants()
        {
            return _partcipantRepository.GetAll();
        }

        [HttpGet("{id}")]
        public ActionResult<Participant> GetParticipant(int id)
        {
            Participant part = _partcipantRepository.GetBy(id);
            if(part == null)
            {
                return NotFound("Participant could not be found for the given id");
            }
            return Ok(part);
        }

        [HttpPut("{id}")]
        public ActionResult<Participant> FollowRegistratedParticipant(int id, Participant part)
        {
            if (id != part.Id)
            {
                return BadRequest("Given id does not match participant id");
            }
            _partcipantRepository.Update(part);
            _partcipantRepository.SaveChanges();
            return Ok(part);
        }

        [HttpGet("{id}/Following")]
        public IEnumerable<Registration> GetFollowing(int id)
        {
            Participant part = _partcipantRepository.GetByWithIncludes(id);
            return _partcipantRepository.getFollowing(part);
        }

        [HttpPut("{id}/Following")]
        public ActionResult<Participant> PutFollowingRegistration(int id, int registrationId)
        {
            Participant part = _partcipantRepository.GetByWithIncludes(id);
            if (part == null)
                return NotFound("Participant could not be found for the given id");
            Registration reg = part.ParticipantRegistrations.Select(pr => pr.Registration).FirstOrDefault(r => r.Id  == registrationId);
            if(reg != null)
                part.RemoveFollowRegistration(reg);
            else
            {
                reg = _registrationRepository.GetBy(registrationId);
                part.AddFollowRegistration(reg);
            }
            
            _context.SaveChanges();
            return Ok();
        }

        //GET: api/{id}/Registrations 
        /// <summary> 
        /// Get registrations from participant
        /// </summary> 
        /// <param name="id">The id of the participant</param>
        [HttpGet("{id}/Registrations")]
        [ProducesResponseType(StatusCodes.Status404NotFound)]
        [ProducesResponseType(StatusCodes.Status200OK)]
        public ActionResult<IEnumerable<RegistrationRouteDTO>> GetRegistrationsOfParticipant(int id)
        {
            IEnumerable<Registration> registrations = _registrationRepository.GetRegistrationsOfParticipant(id);
            if (registrations == null)
                return NotFound("Registrations could not be found for the given id");
            
            return Ok(registrations.Select(r => new RegistrationRouteDTO(r)).ToList());
        }

        //GET: api/Routes 
        /// <summary> 
        /// Get registrated routes from participant
        /// </summary> 
        [HttpGet("{id}/Routes")]
        public IEnumerable<RouteDTO> GetRoutesFromUser(int id)
        {
            return _partcipantRepository.GetRoutesOfParticipant(id).Select(r => new RouteDTO(r));
        }

        [HttpPut("{id}/CanBeFollowed")]
        public ActionResult<Participant> PutCanBeFollowed(int id, bool canBeFollowed)
        {
            Participant part = _partcipantRepository.GetBy(id);
            part.CanBeFollowed = canBeFollowed;
            _partcipantRepository.SaveChanges();
            return Ok(part);
        }
    }
}

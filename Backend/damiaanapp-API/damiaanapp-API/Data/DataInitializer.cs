using damiaanapp_API.Models.Domain;
using damiaanapp_API.Models.Domain.Enum;
using Microsoft.AspNetCore.Identity;
using Microsoft.VisualBasic;
using System;
using System.Collections;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Security.Claims;
using System.Threading.Tasks;
using System.Xml.Linq;

namespace damiaanapp_API.Data
{
    public class DataInitializer
    {
        private readonly ApplicationDbContext _context;
        private readonly UserManager<IdentityUser> _userManager;

        public DataInitializer(ApplicationDbContext context, UserManager<IdentityUser> userManager)
        {
            _context = context;
            _userManager = userManager;
        }

        public async Task InitializeDataAsync()
        {
            
            //_context.Database.EnsureDeleted();
            if (_context.Database.EnsureCreated())
            {

                #region initialize a route
                //initialize some nodes
                Node node1 = new Node
                {
                    NodeType = (NodeType)1,
                    Street = "Pontweg",
                    Number = "1",
                    PostalCode = 9890,
                    City = "Gavere",
                    Longitude = 3.6434986,
                    Latitude = 50.9304008,
                    Info = "testnode1"
                };

                Node node2 = new Node
                {
                    NodeType = (NodeType)2,
                    Street = "Bernstraat",
                    Number = "1",
                    PostalCode = 9890,
                    City = "Gavere",
                    Longitude = 3.6434986,
                    Latitude = 50.9304008,
                    Info = "testnode2"
                };


                //initialize a route
                Route route1 = new Route
                {
                    Name = "GavereRoute",
                    Distance = 10,
                    Start = DateAndTime.Now.AddDays(7),
                    KML = XDocument.Load(Path.Combine(Environment.CurrentDirectory, @"Data\TempXML\", "route.kml")),
                    Nodes = new List<Node>() { node1, node2 },
                    Info = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Mauris tincidunt dui in tincidunt tristique. Aliquam sed ipsum eleifend, porttitor lorem quis, varius metus. Morbi varius, ipsum quis molestie lobortis, velit erat maximus mi, et rhoncus odio purus eget turpis. Morbi erat ligula, blandit porta tellus a, placerat hendrerit leo. Vestibulum rhoncus enim nec viverra viverra. Integer quis nunc mauris."
                };
                
                Route route2 = new Route()
                {
                    Name = "Route2",
                    Distance = 25,
                    Start = DateTime.Now.AddMonths(2),
                    KML = XDocument.Load(Path.Combine(Environment.CurrentDirectory, @"Data\TempXML\", "route.kml")),
                    Nodes = new List<Node>() { },
                    Info = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Mauris tincidunt dui in tincidunt tristique. Aliquam sed ipsum eleifend, porttitor lorem quis, varius metus. Morbi varius, ipsum quis molestie lobortis, velit erat maximus mi, et rhoncus odio purus eget turpis. Morbi erat ligula, blandit porta tellus a, placerat hendrerit leo. Vestibulum rhoncus enim nec viverra viverra. Integer quis nunc mauris."
                };
                
                _context.Routes.Add(route1);
                _context.Routes.Add(route2);
                _context.SaveChanges();
                #endregion

                #region initialize participants
                Participant p1 = new Participant("Goubert", "Brent", "brentgoubert@mail.com", DateAndTime.Now.AddYears(-18), "molenstraat", "2a1", "gent", 9000,true);
                Participant p2 = new Participant("Steenhaut", "Thibaud", "thibaudsteenhaud@mail.com", DateAndTime.Now.AddYears(-19), "bergstraat", "2a1", "gent", 9000,true);
                Participant p3 = new Participant("Paelinck", "Pieter", "pieterpaelinck@mail.com", DateAndTime.Now.AddYears(-20), "kerkstraat", "2a1", "gent", 9000,false);
                Participant p4 = new Participant("Bergmans", "Joke", "jokebergmans@mail.com", DateAndTime.Now.AddYears(-21), "stationstraat", "2a1", "gent", 9000, true);
                Participant p5 = new Participant("Van Der Weeën", "Tom", "tomvanderweeen@mail.com", DateAndTime.Now.AddYears(-22), "poelstraat", "2a1", "gent", 9000, true);
                Participant p6 = new Participant("De Torck", "Boris", "borisdetorck@mail.com", DateAndTime.Now.AddYears(-23), "pleinstraat", "2a1", "gent", 9000, false);
                Participant admin = new Participant("Admin", "Admin", "admin@mail.com", DateAndTime.Now.AddYears(-99), "adminstraat", "1", "Gent", 9000, false);
                

                await CreateUser(p1.Email, "P@ssword1111!");
                await CreateUser(p2.Email, "P@ssword1111!");
                await CreateUser(p3.Email, "P@ssword1111!");
                await CreateUser(p4.Email, "P@ssword1111!");
                await CreateUser(p5.Email, "P@ssword1111!");
                await CreateUser(p6.Email, "P@ssword1111!");
                await CreateAdmin(admin.Email, "P@ssword1111!");

                _context.Participants.Add(p1);
                _context.Participants.Add(p2);
                _context.Participants.Add(p3);
                _context.Participants.Add(p4);
                _context.Participants.Add(p5);
                _context.Participants.Add(p6);
                _context.Participants.Add(admin);

                _context.SaveChanges();

                #endregion

                #region initialize registrations

                Registration r1 = new Registration
                {
                    Paid = true,
                    Route = route1,
                    Participant = p1,
                    Position = new Coordinate(node1.Longitude, node1.Latitude),
                    Timestamp = DateTime.Now,
                    TShirt = true,
                    Tshirtsize = "S"
                };
                Registration r2 = new Registration
                {
                    Paid = false,
                    Route = route1,
                    Participant = p2,
                    Position = new Coordinate(node2.Longitude, node2.Latitude),
                    Timestamp = DateTime.Now,
                    TShirt = true,
                    Tshirtsize = "M"
                };

                Registration r3 = new Registration
                {
                    Paid = true,
                    Route = route1,
                    Participant = p3,
                    Position = new Coordinate(node1.Longitude, node1.Latitude),
                    Timestamp = DateTime.Now,
                    TShirt = false,
                };

                Registration r4 = new Registration
                {
                    Paid = false,
                    Route = route1,
                    Participant = p4,
                    Position = new Coordinate(node1.Longitude, node1.Latitude),
                    Timestamp = DateTime.Now,
                    TShirt = false
                };
                Registration r5 = new Registration
                {
                    Paid = false,
                    Route = route2,
                    Participant = p1,
                    Position = new Coordinate(node1.Longitude, node1.Latitude),
                    Timestamp = DateTime.Now,
                    TShirt = false
                };

               // r1.Scans.Add(new Scan(_context.Nodes.First(), false));
                

                _context.Registrations.Add(r1);
                _context.Registrations.Add(r2);
                _context.Registrations.Add(r3);
                _context.Registrations.Add(r4);
                _context.Registrations.Add(r5);

                _context.SaveChanges();

                #endregion

                #region FollowRegistration
                p1.AddFollowRegistration(r4);
                p1.AddFollowRegistration(r3);

                _context.SaveChanges();
                #endregion
                
            }
        }
        private async Task CreateUser(string email, string password)
        {
            var user = new IdentityUser { UserName = email, Email = email };
            await _userManager.CreateAsync(user, password);
            await _userManager.AddClaimAsync(user, new Claim(ClaimTypes.Role, "customer"));
        }

        private async Task CreateAdmin(string email, string password)
        {
            var user = new IdentityUser { UserName = email, Email = email };
            await _userManager.CreateAsync(user, password);
            await _userManager.AddClaimAsync(user, new Claim(ClaimTypes.Role, "admin"));
        }
    }
}

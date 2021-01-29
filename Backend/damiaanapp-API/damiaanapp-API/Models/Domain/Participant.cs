using System;
using System.Collections.Generic;
using System.Linq;

namespace damiaanapp_API.Models.Domain
{
    public class Participant
    {
        #region Properties
        public int Id { get; set; }
        public string Name { get; set; }
        public string FirstName { get; set; }
        public string Email { get; set; }
        public DateTime BirthDate { get; set; }
        public string Street { get; set; }
        public string Number { get; set; }
        public string City { get; set; }
        public int PostalCode { get; set; }
        public ICollection<ParticipantRegistration> ParticipantRegistrations { get; set; }
        public IEnumerable<Registration> FollowingRegistrations => ParticipantRegistrations.Select(r => r.Registration);
        public Boolean CanBeFollowed { get; set; }

        #endregion

        #region Constructors
        public Participant()
        {
            ParticipantRegistrations = new List<ParticipantRegistration>();
            CanBeFollowed = true;
        }

        public Participant(string name, string firstName, string email, DateTime birthDate, string street, string number, string city, int postalcode, Boolean canBeFollowed)
        {
            ParticipantRegistrations = new List<ParticipantRegistration>();
            Name = name;
            FirstName = firstName;
            Email = email;
            BirthDate = birthDate;
            Street = street;
            Number = number;
            City = city;
            PostalCode = postalcode;
            CanBeFollowed = canBeFollowed;
        }
        #endregion

        #region Methods              
        public void AddFollowRegistration(Registration registration)
        {
            ParticipantRegistrations.Add(
                new ParticipantRegistration()
                {
                    RegistrationId = registration.Id,
                    ParticipantId = this.Id,
                    Registration = registration,
                    Participant = this
                });
        }

        public void RemoveFollowRegistration(Registration registration)
        {
            ParticipantRegistration partReg = ParticipantRegistrations.FirstOrDefault(pr => pr.RegistrationId == registration.Id);
            ParticipantRegistrations.Remove(partReg);
        }
        #endregion
    }
}

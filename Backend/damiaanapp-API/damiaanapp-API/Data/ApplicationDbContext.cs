using damiaanapp_API.Data.Mappers;
using damiaanapp_API.Models.Domain;
using Microsoft.AspNetCore.Identity.EntityFrameworkCore;
using Microsoft.EntityFrameworkCore;

namespace damiaanapp_API.Data
{
    public class ApplicationDbContext : IdentityDbContext
    {
        public DbSet<Route> Routes { get; set; }
        public DbSet<Registration> Registrations { get; set; }
        public DbSet<Participant> Participants { get; set; }
        public DbSet<Node> Nodes { get; set; }

        public ApplicationDbContext(DbContextOptions<ApplicationDbContext> options) : base(options)
        {

        }

        protected override void OnModelCreating(ModelBuilder builder)
        {
            base.OnModelCreating(builder);
            builder.ApplyConfiguration(new RouteConfiguration());
            builder.ApplyConfiguration(new RegistrationConfiguration());
            builder.ApplyConfiguration(new ParticipantRegistrationConfiguration());
            builder.ApplyConfiguration(new ParticipantConfiguration());

        }
    }
}

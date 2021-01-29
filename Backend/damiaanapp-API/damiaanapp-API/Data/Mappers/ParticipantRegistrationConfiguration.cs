using damiaanapp_API.Models.Domain;
using Microsoft.EntityFrameworkCore;
using Microsoft.EntityFrameworkCore.Metadata.Builders;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace damiaanapp_API.Data.Mappers
{
    public class ParticipantRegistrationConfiguration : IEntityTypeConfiguration<ParticipantRegistration>
    {
        public void Configure(EntityTypeBuilder<ParticipantRegistration> builder)
        {
            builder.HasKey(k => new { k.ParticipantId, k.RegistrationId });
            builder.HasOne(p => p.Participant).WithMany(r => r.ParticipantRegistrations).HasForeignKey(p => p.ParticipantId).OnDelete(DeleteBehavior.NoAction);
            builder.HasOne(r => r.Registration).WithMany().HasForeignKey(r => r.RegistrationId).OnDelete(DeleteBehavior.NoAction);
        }
    }
}

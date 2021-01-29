using damiaanapp_API.Models.Domain;
using Microsoft.EntityFrameworkCore;
using Microsoft.EntityFrameworkCore.Metadata.Builders;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace damiaanapp_API.Data.Mappers
{
    public class RegistrationConfiguration : IEntityTypeConfiguration<Registration>
    {
        public void Configure(EntityTypeBuilder<Registration> builder)
        {
            builder
           .HasOne(p => p.Participant)
           .WithMany()
           .IsRequired();

            builder
                .HasMany(r => r.Scans)
                .WithOne();
        }
    }
}

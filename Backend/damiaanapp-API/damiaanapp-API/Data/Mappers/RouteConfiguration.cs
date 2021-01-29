using damiaanapp_API.Models.Domain;
using Microsoft.EntityFrameworkCore;
using Microsoft.EntityFrameworkCore.Metadata.Builders;
using System.Xml.Linq;

namespace damiaanapp_API.Data.Mappers
{
    public class RouteConfiguration : IEntityTypeConfiguration<Route>
    {
        public void Configure(EntityTypeBuilder<Route> builder)
        {
            builder.Property(r => r.KML)
                .HasColumnType("xml")
                .HasConversion(
                k => k.ToString(),
                k => XDocument.Parse(k));

            builder.HasMany(r => r.Nodes)
                .WithOne();
        }
    }
}

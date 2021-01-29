using damiaanapp_API.Models.Domain;
using damiaanapp_API.Services;
using System;
using System.Collections.Generic;

namespace damiaanapp_API.DTOs
{
    public class RouteDTO
    {
        #region Properties
        public int Id { get; set; }
        public string Name { get; set; }
        public int Distance { get; set; }
        public DateTime Start { get; set; }
        public string KML { get; set; }
        public IList<Node> Nodes { get; set;}
        public string Url { get; set; }
        public string Info { get; set; }
        #endregion

        #region Constructors
        public RouteDTO()
        {

        }

        public RouteDTO(Route route)
        {
            Id = route.Id;
            Name = route.Name;
            Distance = route.Distance;
            Start = route.Start;
            KML = Base64Encoder.Base64Encode(route.KML.ToString());
            Nodes = route.Nodes;
            Info = route.Info;
            Url = route.Url;
        }
        #endregion
    }
}

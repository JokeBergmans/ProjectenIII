using damiaanapp_API.DTOs;
using damiaanapp_API.Services;
using System;
using System.Collections.Generic;
using System.Xml.Linq;

namespace damiaanapp_API.Models.Domain
{
    public class Route
    {
        #region Properties
        public int Id { get; set; }
        public string Name { get; set; }
        public int Distance { get; set; }
        public DateTime Start { get; set; }
        public IList<Node> Nodes { get; set; }
        public XDocument KML { get; set; }
        public string Url { get; set; }
        public string Info { get; set; }


        #endregion

        #region Constructors
        public Route()
        {
            Nodes = new List<Node>();
        }

        public Route(int distance, DateTime start)
        {
            Distance = distance;
            Start = start;
            Nodes = new List<Node>();
        }

        #endregion

        #region Methods
        public void UpdateFromDTO(RouteDTO routeDTO)
        {
            Id = routeDTO.Id;
            Name = routeDTO.Name;
            Distance = routeDTO.Distance;
            Start = routeDTO.Start;
            KML = XDocument.Parse(Base64Encoder.Base64Decode(routeDTO.KML));
            Info = routeDTO.Info;
        }

        public void AddNode(Node node)
        {
            Nodes.Add(node);
        }

        public void RemoveNode(Node node)
        {
            Nodes.Remove(node);
        }

        public void RemoveAllNodes()
        {
            Nodes.Clear();
        }
        #endregion
    }
}

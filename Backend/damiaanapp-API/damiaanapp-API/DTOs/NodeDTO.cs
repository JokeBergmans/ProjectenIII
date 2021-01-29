using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using damiaanapp_API.Models.Domain;
using Microsoft.AspNetCore.Mvc;

namespace damiaanapp_API.DTOs
{
    public class NodeDTO 
    {
        #region Properties
        public int Id { get; set; }
        public int NodeType { get; set; }
        public string Street { get; set; }
        public string Number { get; set; }
        public string City { get; set; }
        public int PostalCode { get; set; }
        public double Longitude { get; set; }
        public double Latitude { get; set; }
        public string Info { get; set; }
        #endregion

        #region Contructors
        public NodeDTO()
        {

        }

        public NodeDTO(Node node)
        {
            this.NodeType = (int) node.NodeType;
            this.Street = node.Street;
            this.Number = node.Number;
            this.City = node.City;
            this.PostalCode = node.PostalCode;
            this.Longitude = node.Longitude;
            this.Latitude = node.Latitude;
            this.Info = node.Info;
        }
        #endregion
    }
}

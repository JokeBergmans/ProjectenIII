using damiaanapp_API.DTOs;
using damiaanapp_API.Models.Domain.Enum;
using System;

namespace damiaanapp_API.Models.Domain
{
    public class Node
    {
        #region Properties
        public int Id { get; set; }
        public NodeType NodeType { get; set; }
        public string Street { get; set; }
        public string Number { get; set; }
        public string City { get; set; }
        public int PostalCode { get; set; }
        public double Longitude { get; set; }
        public double Latitude { get; set; }
        public string Info { get; set; }
        #endregion

        #region Contructors
        public Node()
        {

        }

        public Node(NodeType nodetype, String street, String number, String city, int postalcode, double longitude, double latitude, string info)
        {
            this.NodeType = nodetype;
            this.Street = street;
            this.Number = number;
            this.City = city;
            this.PostalCode = postalcode;
            //this.Coordinate = coordinate;
            this.Longitude = longitude;
            this.Latitude = latitude;
            this.Info = info;
        }
        #endregion

        #region Methods
        public void UpdateFromDTO(NodeDTO nodeDTO)
        {
            Id = nodeDTO.Id;
            NodeType = (NodeType)nodeDTO.NodeType;
            Street = nodeDTO.Street;
            Number = nodeDTO.Number;
            City = nodeDTO.City;
            PostalCode = nodeDTO.PostalCode;
            Longitude = nodeDTO.Longitude;
            Latitude = nodeDTO.Latitude;
            Info = nodeDTO.Info;
            #endregion
        }
    }
}

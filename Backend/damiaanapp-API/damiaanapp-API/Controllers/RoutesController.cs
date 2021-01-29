using Azure.Storage.Blobs;
using damiaanapp_API.DTOs;
using damiaanapp_API.Models;
using damiaanapp_API.Models.Domain;
using damiaanapp_API.Services;
using Microsoft.AspNetCore.Authentication.JwtBearer;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Hosting;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Identity;
using Microsoft.AspNetCore.Mvc;
using Microsoft.Azure;
using Microsoft.VisualBasic;
using Microsoft.WindowsAzure.Storage;
using Microsoft.WindowsAzure.Storage.Blob;
using System.Collections.Generic;
using System.Diagnostics;
using System.IO;
using System.Security.Claims;
using System.Threading.Tasks;
using System.Xml.Linq;

namespace damiaanapp_API.Controllers
{
    [ApiConventionType(typeof(DefaultApiConventions))]
    [Produces("application/json")]
    [Route("api/[controller]")]
    [Authorize(AuthenticationSchemes = JwtBearerDefaults.AuthenticationScheme)]
    [ApiController]
    public class RoutesController : ControllerBase
    {
        #region Fields 
        private readonly IRouteRepository _routeRepository;
        private readonly IRegistrationRepository _registrationRepository;
        private readonly IParticipantRepository _participantRepository;
        private readonly INodeRepository _nodeRepository;
        private readonly UserManager<IdentityUser> _userManager;
        private readonly IHostingEnvironment _environment;
        #endregion

        #region Constructors 
        public RoutesController(IRouteRepository routeRepository, IRegistrationRepository registrationRepository, IParticipantRepository participantRepository, INodeRepository nodeRepository, IHostingEnvironment _environment, UserManager<IdentityUser> userManager)
        {
            _routeRepository = routeRepository;
            _registrationRepository = registrationRepository;
            _participantRepository = participantRepository;
            _nodeRepository = nodeRepository;
            _userManager = userManager;
            this._environment = _environment;
        }
        #endregion

        #region Methods 

        //GET: api/Routes 
        /// <summary> 
        /// Get all routes 
        /// </summary> 
        [HttpGet]
        [AllowAnonymous]
        public ActionResult<IEnumerable<Route>> GetRoutes()
        {
            return Ok(_routeRepository.GetAll());
        }


        //GET: api/Routes/{id} 
        /// <summary> 
        /// Get a route 
        /// </summary> 
        /// <param name="id">The id of the route</param> 
        [HttpGet("{id}")]
        [ProducesResponseType(StatusCodes.Status404NotFound)]
        [ProducesResponseType(StatusCodes.Status200OK)]
        [AllowAnonymous]
        public ActionResult<RouteDTO> GetRoute(int id)
        {
            Route route = _routeRepository.GetByIncludeNodes(id);
            if (route == null)
                return NotFound();
            return Ok(new RouteDTO(route));
        }

        //POST: api/Routes 
        /// <summary> 
        /// Add a new route 
        /// </summary> 
        /// <param name="routeDTO">The route to add</param> 
        [HttpPost]
        [ProducesResponseType(StatusCodes.Status201Created)]
        [Authorize(Policy = "Admin")] 
        public async Task<ActionResult<RouteDTO>> PostRouteAsync(RouteDTO routeDTO)
        {
            Route route = new Route
            {
                Name = routeDTO.Name,
                Distance = routeDTO.Distance,
                Start = routeDTO.Start,
                KML = XDocument.Parse(Base64Encoder.Base64Decode(routeDTO.KML)),
                Url = routeDTO.Url,
                Info = routeDTO.Info
            };

            XDocument kml = XDocument.Parse(Base64Encoder.Base64Decode(routeDTO.KML));
            string path = Path.Combine(this._environment.WebRootPath, "routes");
            string fileName = routeDTO.Name + ".kml";
            using (FileStream stream = new FileStream(Path.Combine(path, fileName), FileMode.Create))
            {
                kml.Save(stream);
            }

            string connectionString = "DefaultEndpointsProtocol=https;AccountName=damiaanappstorage2;AccountKey=aY16Ro+rrzuRBt/wET1Nig61sbGHspNOzBDfEbsUO6m0LWbfRgZk30PFLrf7Nh3tnkBDk8DGqa+w30D+vZu1AQ==;BlobEndpoint=https://damiaanappstorage2.blob.core.windows.net/;TableEndpoint=https://damiaanappstorage2.table.core.windows.net/;QueueEndpoint=https://damiaanappstorage2.queue.core.windows.net/;FileEndpoint=https://damiaanappstorage2.file.core.windows.net/";

            BlobServiceClient blobServiceClient = new BlobServiceClient(connectionString);
            //BlobContainerClient containerClient = await blobServiceClient.CreateBlobContainerAsync("routes"); 
            BlobContainerClient containerClient = new BlobContainerClient(connectionString, "routes");
            await containerClient.CreateIfNotExistsAsync();

            string localFilePath = Path.Combine(path, fileName);

            BlobClient blobClient = containerClient.GetBlobClient(fileName);
            // Open the file and upload its data 
            using FileStream uploadFileStream = new FileStream(Path.Combine(path, fileName), FileMode.Open);
            await blobClient.UploadAsync(uploadFileStream, true);
            uploadFileStream.Close();

            _routeRepository.Add(route);
            _routeRepository.SaveChanges();
            return CreatedAtAction(nameof(GetRoute), new { id = route.Id }, new RouteDTO(route));
        }

        //PUT: api/Routes/{id} 
        /// <summary> 
        /// Update a route 
        /// </summary> 
        /// <param name="id">The id of the updated route</param> 
        /// <param name="routeDTO">The updated route</param> 
        [HttpPut("{id}")]
        [ProducesResponseType(StatusCodes.Status200OK)]
        [ProducesResponseType(StatusCodes.Status400BadRequest)]
        [Authorize(Policy = "Admin")]
        public ActionResult<Route> PutRoute(int id, RouteDTO routeDTO)
        {
            if (id != routeDTO.Id)
                return BadRequest("Given id does not match route id");

            Route route = _routeRepository.GetBy(id);

            if (route != null)
                route.UpdateFromDTO(routeDTO);

            _routeRepository.Update(route);
            _routeRepository.SaveChanges();
            return Ok(route);
        }

        //DELETE: api/Routes/{id} 
        /// <summary> 
        /// Delete a route 
        /// </summary> 
        /// <param name="id">The id of the route to delete</param> 
        [HttpDelete("{id}")]
        [ProducesResponseType(StatusCodes.Status204NoContent)]
        [ProducesResponseType(StatusCodes.Status404NotFound)]
        [Authorize(Policy = "Admin")] 
        public ActionResult<Route> DeleteRoute(int id)
        {

            if (id <= 0)
                return BadRequest("Not a valid route id");

            Route route = _routeRepository.GetBy(id);

            if (route == null)
                return NotFound("Route could not be found with the given id");

            _routeRepository.Delete(route);
            _routeRepository.SaveChanges();
            return Ok();
        }

        // GET: api/Routes/{id}/Registrations/ 
        /// <summary> 
        /// Get all registrations for a route 
        /// </summary> 
        /// <param name="id">id of the route</param> 
        [HttpGet("{id}/Registrations")]
        public ActionResult<IEnumerable<Registration>> GetRegistrationsFromRoute(int id)
        {
            Route route = _routeRepository.GetBy(id);
            if (route == null)
                return NotFound("Route could not be found for the given id");
            return Ok(_registrationRepository.GetAll(id));
        }

        // GET: api/Routes/{id}/Registrations/{registrationId} 
        /// <summary> 
        /// Get a registration for a route 
        /// </summary> 
        /// <param name="id">id of the route</param> 
        /// <param name="registrationId">id of the registration</param> 
        [HttpGet("{id}/Registrations/{registrationId}")]
        public ActionResult<Registration> GetRegistration(int id, int registrationId)
        {
            Route route = _routeRepository.GetBy(id);
            if (route == null)
                return NotFound("Route could not be found for the given id");
            Registration registration = _registrationRepository.GetBy(registrationId);
            if (registration == null)
                return NotFound("Registration could not be found for the given id");
            return registration;
        }

        // POST: api/Routes/{id}/Registrations 
        /// <summary> 
        /// Adds a registration to a route 
        /// </summary> 
        /// <param name="id">the id of the route</param> 
        [HttpPost("{id}/Registrations")]
        public ActionResult<Registration> PostRegistration(int id, RegistrationDTO registrationDTO)
        {

            Route route = _routeRepository.GetBy(id);
            if (route == null)
                return NotFound("Route could not be found for the given id");
            Registration registration = new Registration(route, _participantRepository.GetByEmail(User.Identity.Name));
            registration.UpdateFromDTO(registrationDTO);
            _registrationRepository.Add(registration);
            _registrationRepository.SaveChanges();
            return CreatedAtAction("GetRegistration", new { id = route.Id, registrationId = registration.Id }, registration);
        }

        //PUT: api/Routes/{id}/Registrations/{registrationsId} 
        /// <summary> 
        /// Update a route 
        /// </summary> 
        /// <param name="id">The id of the route</param> 
        /// <param name="registrationId">The id of the updated registration</param> 
        /// <param name="registrationDTO">The updated registration</param> 
        [HttpPut("{id}/Registrations/{registrationId}")]
        [ProducesResponseType(StatusCodes.Status200OK)]
        [ProducesResponseType(StatusCodes.Status400BadRequest)]
        public ActionResult<Route> PutRegistration(int id, int registrationId, RegistrationDTO registrationDTO)
        {
            if (id != registrationDTO.Id)
                return BadRequest("Given id does not match registration id");

            Registration registration = _registrationRepository.GetBy(registrationId);

            if (registration != null)
                registration.UpdateFromDTO(registrationDTO);

            _registrationRepository.Update(registration);
            _registrationRepository.SaveChanges();
            return Ok(registration);
        }

        //DELETE: api/Routes/{id}/Registrations/{registrationId} 
        /// <summary> 
        /// Delete a registration 
        /// </summary> 
        /// <param name="id">The id of the route</param> 
        /// <param name="registrationId">The id of the registration to delete</param> 
        [HttpDelete("{id}/Registrations/{registrationId}")]
        [ProducesResponseType(StatusCodes.Status204NoContent)]
        [ProducesResponseType(StatusCodes.Status404NotFound)]
        public ActionResult<Route> DeleteRegistration(int id, int registrationId)
        {
            Registration registration = _registrationRepository.GetBy(registrationId);
            if (registration == null)
                return NotFound("Registration could not be found for the given id");
            _registrationRepository.Delete(registration);
            _registrationRepository.SaveChanges();
            return NoContent();
        }

        //GET: api/Routes/{id}/Nodes 
        /// <summary> 
        /// Return all nodes for a route 
        /// </summary> 
        /// <param name="id"">The id of the route</param> 
        [HttpGet("{id}/Nodes")]
        [AllowAnonymous]
        public ActionResult<IEnumerable<Node>> GetAllNodes(int id)
        {
            return Ok(_nodeRepository.GetAll(id));
        }

        //GET: api/Routes/{id}/Nodes/{id} 
        /// <summary> 
        /// Return a node by id 
        /// </summary> 
        /// <param name="id">route id</param> 
        /// <param name="nodeId">node id</param> 
        [HttpGet("{id}/Nodes/{nodeId}")]
        [ProducesResponseType(StatusCodes.Status404NotFound)]
        [ProducesResponseType(StatusCodes.Status200OK)]
        [AllowAnonymous]
        public ActionResult<NodeDTO> GetNode(int id, int nodeId)
        {
            Node node = _nodeRepository.GetBy(nodeId);
            if (node == null)
                return NotFound("Node could not be found for the given id");
            //return Ok(new NodeDTO(node));
            return Ok(node);
        }

        //POST: api/Routes/{id}/Nodes 
        /// <summary> 
        /// Adds a new node 
        /// </summary> 
        /// <param name="nodeDTO">the new node to add</param> 
        [HttpPost("{id}/Nodes")]
        [ProducesResponseType(StatusCodes.Status201Created)]
        [Authorize(Policy = "Admin")]
        public ActionResult<NodeDTO> PostNode(int id, NodeDTO nodeDTO)
        {
            Route route = _routeRepository.GetBy(id);
            if (route == null)
            {
                return NotFound("Route could not be found for the given id");
            }

            Node n = new Node
            {
                NodeType = (Models.Domain.Enum.NodeType)nodeDTO.NodeType,
                Street = nodeDTO.Street,
                Number = nodeDTO.Number,
                PostalCode = nodeDTO.PostalCode,
                City = nodeDTO.City,
                Longitude = nodeDTO.Longitude,
                Latitude = nodeDTO.Latitude,
                Info = nodeDTO.Info
            };

            route.AddNode(n);
            _routeRepository.Update(route);
            _routeRepository.SaveChanges();
            //return CreatedAtAction(nameof(GetNode), new { id = n.Id }, n);
            return Ok(n);
        }

        //PUT: api/Routes/{id}/Nodes/{nodeId} 
        /// <summary> 
        /// Update a node by id 
        /// </summary> 
        /// <param name="id">id of the route</param> 
        /// <param name="nodeId">id of the node to update</param> 
        /// <param name="nodeDTO">updated node</param> 
        [HttpPut("{id}/Nodes/{nodeId}")]
        [ProducesResponseType(StatusCodes.Status200OK)]
        [ProducesResponseType(StatusCodes.Status400BadRequest)]
        [Authorize(Policy = "Admin")]
        public ActionResult<Node> UpdateNode(int id, int nodeId, NodeDTO nodeDTO)
        {
            if (nodeId != nodeDTO.Id)
                return BadRequest("Given id does not match node id");

            Node n = _nodeRepository.GetBy(nodeId);

            if (n != null)
                n.UpdateFromDTO(nodeDTO);

            _nodeRepository.Update(n);
            _nodeRepository.SaveChanges();
            return Ok(n);
        }

        //DELETE: api/Routes/{id}/Nodes/{nodeId} 
        //DELETE: api/Routes/{id}/Nodes/{nodeId} 
        /// <summary> 
        /// Delete node by id 
        /// </summary> 
        /// <param name="id">id of the route</param> 
        /// <param name="nodeId">id of the node to delete</param> 
        [HttpDelete("{id}/Nodes/{nodeId}")]
        [ProducesResponseType(StatusCodes.Status204NoContent)]
        [ProducesResponseType(StatusCodes.Status404NotFound)]
        [Authorize(Policy = "Admin")]
        public ActionResult<Route> DeleteNode(int id, int nodeId)
        {
            Route route = _routeRepository.GetBy(id);
            if (route == null)
                return NotFound("Route could not be found for the given id");
            Node node = _nodeRepository.GetBy(nodeId);
            route.RemoveNode(node);
            _routeRepository.Update(route);
            _routeRepository.SaveChanges();
            _nodeRepository.Delete(node);
            _nodeRepository.SaveChanges();
            return Ok();
        }

        //DELETE: api/Routes/{id}/Nodes
        //DELETE: api/Routes/{id}/Nodes
        /// <summary> 
        /// Deletes all nodes 
        /// </summary> 
        /// <param name="id">id of the route</param> 
        /// <param name="nodeId">id of the node to delete</param> 
        [HttpDelete("{id}/Nodes")]
        [ProducesResponseType(StatusCodes.Status204NoContent)]
        [ProducesResponseType(StatusCodes.Status404NotFound)]
        [AllowAnonymous]
        public ActionResult<Route> DeleteNodes(int id)
        {
            Route route = _routeRepository.GetByIncludeNodes(id);
            if (route == null)
                return NotFound("Route could not be found for the given id");

            _nodeRepository.RemoveAll(route.Nodes); 
            _routeRepository.Update(route);
            _nodeRepository.SaveChanges();
            _routeRepository.SaveChanges();
            return Ok();
        }



        //POST: api/Routes/{id}/Registrations/{registrationId}/Nodes 
        /// <summary> 
        /// Adds a new scan 
        /// </summary> 
        /// <param name="registrationId">id of the participant</param>
        /// <param name="nodeId">id of the scanned node (QR)</param>
        [HttpPost("{id}/Registrations/{registrationId}/Scanned")]
        [AllowAnonymous]
        public ActionResult<Scan> AddScan(int id, int registrationId, int nodeId)
        {

            Node scannedNode = _nodeRepository.GetBy(nodeId);

            if (scannedNode == null) {
                return NotFound("Node could not be found for the given id");
            }

            Scan scanToAdd = new Scan(scannedNode);
            _registrationRepository.AddScan(registrationId, scanToAdd);
            _registrationRepository.SaveChanges();
            return Ok();

        }

        //GET: api/Routes/{id}/Scanned/{registrationId} 
        /// <summary> 
        /// Returns all the nodes that a participant has scanned on a route
        /// </summary> 
        /// <param name="registrationId"> id of the registration </param>
        [HttpGet("{id}/Registrations/{registrationId}/Scanned")]
        [AllowAnonymous]
        public ActionResult<ICollection<Scan>> GetScannedNodes(int id, int registrationId)
        {

            ICollection<Scan> scannedNodes = _registrationRepository.getScansBy(registrationId);

            if(scannedNodes == null) {
                return NotFound("Registration could not be found for the given id");
            }

            return Ok(scannedNodes); 
        }
        #endregion
    }
}
using damiaanapp_API.Models;
using damiaanapp_API.Models.Domain;
using Microsoft.EntityFrameworkCore;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace damiaanapp_API.Data.Repositories
{
    public class NodeRepository : INodeRepository
    {

        #region Fields
        private readonly ApplicationDbContext _context;
        private readonly DbSet<Route> _routes;
        private readonly DbSet<Node> _nodes;
        #endregion

        #region Constructors
        public NodeRepository(ApplicationDbContext context)
        {
            _context = context;
            _routes = context.Routes;
            _nodes = context.Nodes;
        }
        #endregion

        #region Methods
        public IEnumerable<Node> GetAll(int id)
        {
            return _routes.Where(r => r.Id == id).SelectMany(r => r.Nodes).OrderBy(n => n.Id).ToList();
        }

        public Node GetBy(int id)
        {
            //return GetAll(routeId).FirstOrDefault(n => n.Id == id);
            return _nodes.FirstOrDefault(n => n.Id == id);
        }      

        public void Update(Node node)
        {
            _nodes.Update(node);
        }

        public void Delete(Node node)
        {
            _nodes.Remove(node);
        }

        public void SaveChanges()
        {
            _context.SaveChanges();
        }

        public void RemoveAll(ICollection<Node> nodes)
        {
            _nodes.RemoveRange(nodes);
        }
        #endregion
    }
}

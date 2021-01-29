using damiaanapp_API.Models;
using damiaanapp_API.Models.Domain;
using Microsoft.EntityFrameworkCore;
using System.Collections.Generic;
using System.Linq;

namespace damiaanapp_API.Data.Repositories
{
    public class RouteRepository : IRouteRepository
    {
        #region Fields
        private readonly ApplicationDbContext _context;
        private readonly DbSet<Route> _routes;
        #endregion

        #region Constructors
        public RouteRepository(ApplicationDbContext context)
        {
            _context = context;
            _routes = context.Routes;
        }
        #endregion

        #region Methods
        public IEnumerable<Route> GetAll()
        {
            return _routes.OrderByDescending(r => r.Start).ToList();
        }

        public Route GetBy(int id)
        {
            return _routes.FirstOrDefault(r => r.Id == id);
        }

        public Route GetByIncludeNodes(int id)
        {
            return _routes.Include(r => r.Nodes).FirstOrDefault(r => r.Id == id);
        }

        public void Add(Route route)
        {
            _routes.Add(route);
        }

        public void Update(Route route)
        {
            _routes.Update(route);
        }

        public void Delete(Route route)
        {
            _routes.Remove(route);
        }

        public void SaveChanges()
        {
            _context.SaveChanges();
        }

        public IEnumerable<Node> GetNodesRoute(int id)
        {
            //Route route = GetBy(id);
            Route route = _routes.Include(r => r.Nodes).FirstOrDefault(r => r.Id == id);
            return route.Nodes;
        }

        #endregion
    }
}

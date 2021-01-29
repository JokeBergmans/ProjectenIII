using damiaanapp_API.Models.Domain;
using System.Collections.Generic;

namespace damiaanapp_API.Models
{
    public interface IRouteRepository
    {
        IEnumerable<Route> GetAll();
        Route GetBy(int id);
        Route GetByIncludeNodes(int id);
        void Add(Route route);
        void Update(Route route);
        void Delete(Route route);
        void SaveChanges();
        IEnumerable<Node> GetNodesRoute(int id);
    }
}

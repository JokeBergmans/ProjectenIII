using damiaanapp_API.Models.Domain;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace damiaanapp_API.Models
{
    public interface INodeRepository
    {
        IEnumerable<Node> GetAll(int id);
        Node GetBy(int id);
        void RemoveAll(ICollection<Node> nodes);
        void Update(Node node);
        void Delete(Node node);
        void SaveChanges();
    }
}

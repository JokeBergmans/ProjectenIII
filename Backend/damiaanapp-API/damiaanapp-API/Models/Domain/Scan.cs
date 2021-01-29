using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace damiaanapp_API.Models.Domain
{
    public class Scan
    {
        public int Id { get; set; }
        public Node Node { get; set; }
        public DateTime TimeScanned { get; set; }

        public Scan()
        {

        }

        public Scan(Node node)
        {  
            Node = node;
            TimeScanned = DateTime.Now;
        }
    }
}

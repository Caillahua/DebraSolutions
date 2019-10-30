using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace CapturaloService.Entity
{
    public class Categoria
    {
        public int idCategoria { get; set; }
        public string descripcion { get; set; }
        public string estado { get; set; }

        public Categoria()
        {

        }
    }
}
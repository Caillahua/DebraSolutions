using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace CapturaloService.Entity
{
    public class Cliente
    {
        public int idCliente { get; set; }
        public int idUsuario { get; set; }
        public string nombre { get; set; }
        public string apellido { get; set; }
        public string dni { get; set; }
        public string direccion { get; set; }
        public string rsocial { get; set; }
        public string ruc { get; set; }
        public decimal credito { get; set; }
        public string estado { get; set; }

    }
}
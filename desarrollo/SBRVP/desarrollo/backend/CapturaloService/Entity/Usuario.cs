using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace CapturaloService.Entity
{
    public class Usuario
    {
        public int idUsuario { get; set; }
        public string usuario { get; set; }
        public string contrasenia { get; set; }
        public string correo { get; set; }
        public string estado { get; set; }
        public string perfil { get; set; }

    }
}
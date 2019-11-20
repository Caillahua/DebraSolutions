<<<<<<< HEAD
using System.Collections.Generic;
using System.Collections.ObjectModel;

namespace CapturaloService.Areas.HelpPage.ModelDescriptions
{
    public class EnumTypeModelDescription : ModelDescription
    {
        public EnumTypeModelDescription()
        {
            Values = new Collection<EnumValueDescription>();
        }

        public Collection<EnumValueDescription> Values { get; private set; }
    }
=======
using System.Collections.Generic;
using System.Collections.ObjectModel;

namespace CapturaloService.Areas.HelpPage.ModelDescriptions
{
    public class EnumTypeModelDescription : ModelDescription
    {
        public EnumTypeModelDescription()
        {
            Values = new Collection<EnumValueDescription>();
        }

        public Collection<EnumValueDescription> Values { get; private set; }
    }
>>>>>>> origin/chacaliazaDebra
}
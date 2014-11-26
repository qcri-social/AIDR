
# Data Transfer Objects (DTOs)

DTOs are used in AIDR to transfer data. In AIDR most DTOs contain all the properties of an entity.

## Types of properties

DTOs in AIDR have mandatory and optional properties.

All mandatory properties must be marked final, so they are initialized by the constructor.

All optional properties must throw an exception of type `PropertyNotSetException`, or a subclass of it, if unset. 

## Mapping

Mapping from entities is done through the constructor and the constructor only.

Mapping to entities is done by a method `toEntity()`. This method is always called the same no matter what the class is.

## Example

Entity:

    @Entity
    @Table(name = "crisis", ...)
    public class Crisis ... {

        private Long crisisId;

    private String name;

    private List<ModelFamily> modelFamilies = null;

        // + getters and setters
    }

DTO:

    @XmlRootElement
    @JsonIgnoreProperties(ignoreUnknown=true)
    public class CrisisDTO implements Serializable {

    /* Mandatory properties */

        @XmlElement
        private final Long crisisID;

        @XmlElement
        private final String name;

        /* Optional properties */

        private List<ModelFamilyDTO> modelFamiliesDTO = null;

        /* Constructors from entity and from properties */

        public CrisisDTO( Crisis crisis ) {
            this.setCrisisID(crisis.getCrisisID());
            this.setName(crisis.getName());
        }

        public CrisisDTO( Long crisisID, String name ) {
            this.setCrisisID(crisisID);
            this.setName(name);
        }

        /* Mapping to entity */

        public Crisis toEntity() {
            Crisis crisis = new Crisis();
            crisis.setCrisisID(getCrisisID());
            crisis.setName(getName());
        }

        /* Getters for mandatory properties */

        public getCrisisID() {
            return crisisID;
        }

        public getName() {
            return name;
        }

        /* Getters for optional properties */
        public getModelFamiliesDTO() {
            if( modelFamiliesDTO == null ) {
                logger.error( "Attempt to access unset property" );
                throw new PropertyNotSetException();
            } else {
                return modelFamiliesDTO;
            }
        }

        /* Setters, which must always verify */

        public setCrisisID(Long crisisID) {
            if( crisisID == null ) {
                throw new IllegalArgumentException("crisisID cannot be null");
            } else if( crisisID.longValue() <= 0 ) {
                throw new IllegalArgumentException("crisisID cannot be zero or a negative number");
            } else {
                this.crisisID = crisisID;
            }
        }
    }
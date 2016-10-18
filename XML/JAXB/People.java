// javac -cp \* People.java && java -cp .:\* People

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.annotation.*;
import java.io.StringReader;
import java.io.StringWriter;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="person" maxOccurs="unbounded" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="name2" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="age" type="{http://www.w3.org/2001/XMLSchema}byte"/>
 *                   &lt;element name="address">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;sequence>
 *                             &lt;element name="street" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                             &lt;element name="nr" type="{http://www.w3.org/2001/XMLSchema}short"/>
 *                           &lt;/sequence>
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                 &lt;/sequence>
 *                 &lt;attribute name="id" type="{http://www.w3.org/2001/XMLSchema}string" />
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "person"
})
@XmlRootElement(name = "people")
public class People {

    public static void main(String args[]) throws JAXBException {
        People people = new People();
        final Person john = new Person();
        john.setName("John");
        john.setName2("Robert");
        john.setAge((byte)44);
        People.Person.Address tmp = new People.Person.Address();
        tmp.nr = 111;
        tmp.street = "Slawkowska";
        john.setAddress(tmp);
        people.getPerson().add(john);
        
        final Person jan = new Person();
        jan.setName("jan");
        jan.setName2("Kowalski");
        jan.setAge((byte)22);
        tmp = new People.Person.Address();
        tmp.nr = 444;
        tmp.street = "Szewska";
        jan.setAddress(tmp);
        
        people.getPerson().add(jan);
        // write it out as XML
        final JAXBContext jaxbContext = JAXBContext.newInstance(People.class);
        StringWriter writer = new StringWriter();
        jaxbContext.createMarshaller().marshal(people, writer);
        System.out.println(writer.toString());
        // read it from XML
        final People johnRead = (People) jaxbContext.createUnmarshaller().unmarshal(new StringReader(writer.toString()));
        
        System.out.println(johnRead.getPerson().get(0).getName());
        System.out.println(johnRead.getPerson().get(1).getName());
    }

    protected List<People.Person> person;

    /**
     * Gets the value of the person property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the person property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getPerson().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link People.Person }
     * 
     * 
     */
    public List<People.Person> getPerson() {
        if (person == null) {
            person = new ArrayList<People.Person>();
        }
        return this.person;
    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="name2" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="age" type="{http://www.w3.org/2001/XMLSchema}byte"/>
     *         &lt;element name="address">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;sequence>
     *                   &lt;element name="street" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *                   &lt;element name="nr" type="{http://www.w3.org/2001/XMLSchema}short"/>
     *                 &lt;/sequence>
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *       &lt;/sequence>
     *       &lt;attribute name="id" type="{http://www.w3.org/2001/XMLSchema}string" />
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "name",
        "name2",
        "age",
        "address"
    })
    @XmlRootElement(name = "person")
    public static class Person {

        @XmlElement(required = true)
        protected String name;
        @XmlElement(required = true)
        protected String name2;
        protected byte age;
        @XmlElement(required = true)
        protected People.Person.Address address;
        @XmlAttribute
        protected String id;

        /**
         * Gets the value of the name property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getName() {
            return name;
        }

        /**
         * Sets the value of the name property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setName(String value) {
            this.name = value;
        }

        /**
         * Gets the value of the name2 property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getName2() {
            return name2;
        }

        /**
         * Sets the value of the name2 property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setName2(String value) {
            this.name2 = value;
        }

        /**
         * Gets the value of the age property.
         * 
         */
        public byte getAge() {
            return age;
        }

        /**
         * Sets the value of the age property.
         * 
         */
        public void setAge(byte value) {
            this.age = value;
        }

        /**
         * Gets the value of the address property.
         * 
         * @return
         *     possible object is
         *     {@link People.Person.Address }
         *     
         */
        public People.Person.Address getAddress() {
            return address;
        }

        /**
         * Sets the value of the address property.
         * 
         * @param value
         *     allowed object is
         *     {@link People.Person.Address }
         *     
         */
        public void setAddress(People.Person.Address value) {
            this.address = value;
        }

        /**
         * Gets the value of the id property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getId() {
            return id;
        }

        /**
         * Sets the value of the id property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setId(String value) {
            this.id = value;
        }


        /**
         * <p>Java class for anonymous complex type.
         * 
         * <p>The following schema fragment specifies the expected content contained within this class.
         * 
         * <pre>
         * &lt;complexType>
         *   &lt;complexContent>
         *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *       &lt;sequence>
         *         &lt;element name="street" type="{http://www.w3.org/2001/XMLSchema}string"/>
         *         &lt;element name="nr" type="{http://www.w3.org/2001/XMLSchema}short"/>
         *       &lt;/sequence>
         *     &lt;/restriction>
         *   &lt;/complexContent>
         * &lt;/complexType>
         * </pre>
         * 
         * 
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {
            "street",
            "nr"
        })
        public static class Address {

            @XmlElement(required = true)
            protected String street;
            protected short nr;

            /**
             * Gets the value of the street property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getStreet() {
                return street;
            }

            /**
             * Sets the value of the street property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setStreet(String value) {
                this.street = value;
            }

            /**
             * Gets the value of the nr property.
             * 
             */
            public short getNr() {
                return nr;
            }

            /**
             * Sets the value of the nr property.
             * 
             */
            public void setNr(short value) {
                this.nr = value;
            }

        }

    }

}

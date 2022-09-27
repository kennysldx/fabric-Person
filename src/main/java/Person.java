import lombok.Data;
import org.hyperledger.fabric.contract.annotation.DataType;
import org.hyperledger.fabric.contract.annotation.Property;

/**
 * author Kennys
 * date 2022/9/26 15:02
 */

@Data
@DataType
public class Person {
    @Property
    String name;

    @Property
    Integer age;

    @Property
    String gender;

    @Property
    String phone;

    public Person() {
    }

    public Person(String name, Integer age, String gender, String phone) {
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.phone = phone;
    }
}

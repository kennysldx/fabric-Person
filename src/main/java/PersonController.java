import com.aliyuncs.utils.StringUtils;
import lombok.extern.java.Log;
import org.hyperledger.fabric.contract.Context;
import org.hyperledger.fabric.contract.ContractInterface;
import org.hyperledger.fabric.contract.annotation.*;
import org.hyperledger.fabric.shim.ChaincodeException;
import org.hyperledger.fabric.shim.ChaincodeStub;

import java.util.Random;
import java.util.logging.Level;

import com.alibaba.fastjson.JSON;

/**
 * author Kennys
 * date 2022/9/25 15:56
 */

@Contract(
        name = "CatContract",
        info = @Info(
                title = "Cat contract",
                description = "The hyperlegendary car contract",
                version = "0.0.1-SNAPSHOT",
                license = @License(
                        name = "Apache 2.0 License",
                        url = "http://www.apache.org/licenses/LICENSE-2.0.html"),
                contact = @Contact(
                        email = "f.carr@example.com",
                        name = "F Carr",
                        url = "https://hyperledger.example.com")))
@Default
@Log
public class PersonController implements ContractInterface {

    @Transaction
    public void initLedger(final Context ctx) {
        ChaincodeStub stub = ctx.getStub();
        for (int i = 0; i < 10; i++ ) {
            String name = "person" + i;
            Integer age = new Random().nextInt();
            String phone = "1575566182" + i;
            String gender="female";
            if(i%2==0) gender = "male";
            Person person = new Person(name,age,gender,phone);
            stub.putStringState(person.getName() , JSON.toJSONString(person));
        }
    }

    @Transaction
    public Person queryPerson(final Context ctx, final String key) {

        ChaincodeStub stub = ctx.getStub();
        String catState = stub.getStringState(key);

        if (StringUtils.isEmpty(catState)) {
            String errorMessage = String.format("Cat %s does not exist", key);
            System.out.println(errorMessage);
            throw new ChaincodeException(errorMessage);
        }

        return JSON.parseObject(catState , Person.class);
    }


    @Transaction
    public Person createPerson(final Context ctx, final String key , String name , Integer age , String gender , String phone) {

        ChaincodeStub stub = ctx.getStub();
        String personState = stub.getStringState(key);

        if (StringUtils.isNotEmpty(personState)) {
            String errorMessage = String.format("person %s already exists", key);
            System.out.println(errorMessage);
            throw new ChaincodeException(errorMessage);
        }

        Person person = new Person(name,age,gender,phone);

        String json = JSON.toJSONString(person);
        stub.putStringState(key, json);

        stub.setEvent("createPersonEvent" , org.apache.commons.codec.binary.StringUtils.getBytesUtf8(json));
        return person;
    }

    @Transaction
    public Person deletePerson(final Context ctx, final String key) {

        ChaincodeStub stub = ctx.getStub();
        String personState = stub.getStringState(key);

        if (StringUtils.isEmpty(personState)) {
            String errorMessage = String.format("Person %s does not exist", key);
            System.out.println(errorMessage);
            throw new ChaincodeException(errorMessage);
        }

        stub.delState(key);

        return JSON.parseObject(personState , Person.class);
    }

}

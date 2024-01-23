package pierpaolo.colasante.u5w2d5project.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pierpaolo.colasante.u5w2d5project.entities.Device;
import pierpaolo.colasante.u5w2d5project.entities.DeviceStatusType;
import pierpaolo.colasante.u5w2d5project.entities.User;
import pierpaolo.colasante.u5w2d5project.exceptions.BadRequestException;
import pierpaolo.colasante.u5w2d5project.exceptions.NotFoundException;
import pierpaolo.colasante.u5w2d5project.payloads.DeviceDTO;
import pierpaolo.colasante.u5w2d5project.repositories.DeviceDAO;

import java.util.List;

@Service
public class DeviceService {
    @Autowired
    private DeviceDAO deviceDAO;
    @Autowired
    private UserService userService;

    public List<Device> getDevice(){return this.deviceDAO.findAll();}
    public Device save(DeviceDTO body){
        User user = userService.findById(body.userId());
        String statusTypeValue = body.statusType();
        try {
            DeviceStatusType statusType = DeviceStatusType.valueOf(statusTypeValue);
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Attenzione!!! " + statusTypeValue + " non è un valore accettabile. I Valori accettabili sono: ASSEGNATO, DISMESSO, DISPONIBILE o MANUTENZIONE");
        }
        Device newDevice = new Device();
        newDevice.setTypologies(body.typologies());
        newDevice.setStatusType(DeviceStatusType.valueOf(body.statusType()));
        newDevice.setUser(user);
        return deviceDAO.save(newDevice);
    }

    public Device findById(long id){
        return deviceDAO.findById(id).orElseThrow(()->new NotFoundException(id));
    }

    public Device findByIdAndUpdate(long id, Device body){
        Device found = this.findById(id);
        found.setTypologies(body.getTypologies());
        found.setStatusType(body.getStatusType());
        return deviceDAO.save(found);
    }

    public void findByIdAndDelete(long id){
        Device found = this.findById(id);
        deviceDAO.delete(found);
    }
}

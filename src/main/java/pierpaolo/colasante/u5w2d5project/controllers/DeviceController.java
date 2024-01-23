package pierpaolo.colasante.u5w2d5project.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pierpaolo.colasante.u5w2d5project.entities.Device;
import pierpaolo.colasante.u5w2d5project.exceptions.BadRequestException;
import pierpaolo.colasante.u5w2d5project.payloads.DeviceDTO;
import pierpaolo.colasante.u5w2d5project.payloads.DeviceResponseDTO;
import pierpaolo.colasante.u5w2d5project.services.DeviceService;

import java.util.List;

@RestController
@RequestMapping("/devices")
public class DeviceController {
    @Autowired
    private DeviceService deviceService;
    @GetMapping
    public List<Device> getDevice(){return deviceService.getDevice();}
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DeviceResponseDTO saveDevice(@RequestBody @Validated DeviceDTO body, BindingResult validation){
        if(validation.hasErrors()){
            throw new BadRequestException(validation.getAllErrors());
        } else {
            Device newDevice = deviceService.save(body);
            return new DeviceResponseDTO(newDevice.getId());
        }
    }
    @GetMapping("/{id}")
    public Device findById(@PathVariable int id){return deviceService.findById(id);}
    @PutMapping("/{id}")
    public Device findIdUpdate(@PathVariable int id, @RequestBody Device body){
        return this.deviceService.findByIdAndUpdate(id, body);
    }
    @DeleteMapping("/{id}")
    public void findIdDelete(@PathVariable int id){ this.deviceService.findByIdAndDelete(id);}
}

package com.workintech.dependencyprojection.rest;


import com.workintech.dependencyprojection.mapping.DeveloperResponse;
import com.workintech.dependencyprojection.model.Developer;
import com.workintech.dependencyprojection.model.JuniorDeveloper;
import com.workintech.dependencyprojection.model.MidDeveloper;
import com.workintech.dependencyprojection.model.SeniorDeveloper;
import com.workintech.dependencyprojection.tax.DeveloperTax;
import com.workintech.dependencyprojection.tax.Taxable;
import com.workintech.dependencyprojection.validation.DeveloperValidation;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/developers")
public class DeveloperController {

    private Map<Integer, Developer> developers;
    private Taxable taxable;

    @PostConstruct
    public void init(){
        developers=new HashMap<>();
    }
    @Autowired
    public DeveloperController(@Qualifier("developerTax") Taxable taxable){
        this.taxable=taxable;
    }

    @GetMapping("/")
    public List<Developer> get(){
        return  developers.values().stream().toList();
    }
    @GetMapping("/{id}")
    public DeveloperResponse getById(@PathVariable int id){
        if(!DeveloperValidation.isIdValid(id)){
            return new DeveloperResponse(null, "Id is not valid", 400);
        }
        if(!developers.containsKey(id)){
            return new DeveloperResponse(null,
                    "Developer with given id is not exist: " + id, 400);
        }
        return new DeveloperResponse(developers.get(id), "Success", 200);

    }

    @PostMapping("/")
  public DeveloperResponse save(@RequestBody Developer developer){
         Developer savedDeveloper=createDeveloper(developer);
        if(savedDeveloper == null){
            return new DeveloperResponse(null,
                    "Developer with given experience is not valid", 400);
        }
        if(developers.containsKey(developer.getId())){
            return new DeveloperResponse(null,
                    "Developer with given id already exist: " + developer.getId(), 400);
        }
        if(!DeveloperValidation.isDeveloperValid(developer)){
            return new DeveloperResponse(null,
                    "Developer credentials are not valid", 400);
        }
        developers.put(developer.getId(), savedDeveloper);
        return new DeveloperResponse(developers.get(developer.getId()), "Succes", 201);
  }
  @PutMapping("/{id}")
  public Developer update(@PathVariable int id,@RequestBody Developer developer){
      if(!developers.containsKey(id)){

      }
      developer.setId(id);
      Developer updateDeveloper=createDeveloper(developer);
      developers.put(id,updateDeveloper);
      return developers.get(id);
  }
  @DeleteMapping("/{id}")
  public Developer delete(@PathVariable int id){
        if(!developers.containsKey(id)){

        }

        Developer developer =developers.get(id);
        developers.remove(id);
        return developer;

  }

  public Developer createDeveloper(Developer developer){
      Developer saveDeveloper;
      if(developer.getExperience().name().equalsIgnoreCase("junior")){
          saveDeveloper=new JuniorDeveloper(developer.getId(),developer.getName(),developer.getSalary()-developer.getSalary()*
                  taxable.getSimpleTaxRate(),developer.getExperience());

      }else if(developer.getExperience().name().equalsIgnoreCase("mid")){
          saveDeveloper=new MidDeveloper(developer.getId(),developer.getName(),developer.getSalary()-developer.getSalary()*
                  taxable.getMiddleTaxRate(),developer.getExperience());

      }else if(developer.getExperience().name().equalsIgnoreCase("senior")){
          saveDeveloper=new SeniorDeveloper(developer.getId(),developer.getName(),developer.getSalary()-developer.getSalary()*
                  taxable.getUpperTaxRate(),developer.getExperience());
      }else{
          saveDeveloper=null;
      }
      return  saveDeveloper;
  }


}

package com.companyx.equity.controller;

import com.companyx.equity.model.Equity;
import com.companyx.equity.repository.EquityRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
public class EquityController {

    @Autowired
    EquityRepository EquityRespository;

    @GetMapping("/Equity")
    public List<Equity> index(){
        return EquityRespository.findAll();
    }

    @GetMapping("/Equity/{id}")
    public Equity show(@PathVariable String id){
        int EquityId = Integer.parseInt(id);
        return EquityRespository.findOne(EquityId);
    }

    @PostMapping("/Equity/search")
    public List<Equity> search(@RequestBody Map<String, String> body){
        String searchTerm = body.get("text");
        return EquityRespository.findByNameContaining(searchTerm);
    }

    @PostMapping("/Equity")
    public Equity create(@RequestBody Map<String, String> body){
        String name = body.get("name");
        return EquityRespository.save(new Equity(name));
    }

    @PutMapping("/Equity/{id}")
    public Equity update(@PathVariable String id, @RequestBody Map<String, String> body){
        int EquityId = Integer.parseInt(id);
        // getting Equity
        Equity Equity = EquityRespository.findOne(EquityId);
        Equity.setName(body.get("name"));
        return EquityRespository.save(Equity);
    }

    @DeleteMapping("Equity/{id}")
    public boolean delete(@PathVariable String id){
        int EquityId = Integer.parseInt(id);
        EquityRespository.delete(EquityId);
        return true;
    }


}
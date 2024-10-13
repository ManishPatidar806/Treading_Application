package com.manish.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.manish.model.Coin;
import com.manish.service.CoinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/coins")
public class CoinController {

    @Autowired
    private CoinService coinService;

    @Autowired
    private ObjectMapper objectMapper;

    @GetMapping
    ResponseEntity<List<Coin>>getCoinList(@RequestParam(required = false, name = "page")int page){
        List<Coin> coinList = coinService.getCoinList(page);
        return new ResponseEntity<>(coinList , HttpStatus.ACCEPTED);

    }

    @GetMapping("/{coinId}/chart")
    ResponseEntity<JsonNode>getMarketChart(@PathVariable String coinId, @RequestParam("days") int days) throws JsonProcessingException {
        String res = coinService.getMarketChart(coinId,days);
        JsonNode node = objectMapper.readTree(res);

        return new ResponseEntity<>(node , HttpStatus.ACCEPTED);

    }

    @GetMapping("/search")
    ResponseEntity<JsonNode>searchCoin( @RequestParam("q") String keyword) throws JsonProcessingException {
        String res = coinService.searchCoin(keyword);
        JsonNode node = objectMapper.readTree(res);

        return ResponseEntity.ok(node);

    }

    @GetMapping("/top50")
    ResponseEntity<JsonNode>getTop50CoinsByMarketCapRank() throws JsonProcessingException {
        String res = coinService.getTop50CoinsByMarketCapRank();
        JsonNode node = objectMapper.readTree(res);
        return ResponseEntity.ok(node);

    }

    @GetMapping("/treading")
    ResponseEntity<JsonNode>getTreadingCoin() throws JsonProcessingException {
        String res = coinService.getTreadingCoins();
        JsonNode node = objectMapper.readTree(res);
        return ResponseEntity.ok(node);

    }

    @GetMapping("/details/{coinId}")
    ResponseEntity<JsonNode>getCoinDetails(@PathVariable String coinId) throws JsonProcessingException {
        String res = coinService.getCoinDetails(coinId);
        JsonNode node = objectMapper.readTree(res);
        return ResponseEntity.ok(node);

    }







}

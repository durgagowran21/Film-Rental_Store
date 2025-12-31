package com.example.filmrental.service;
 
import java.time.LocalDate;

import java.util.HashMap;

import java.util.List;

import java.util.Map;
 
import org.modelmapper.ModelMapper;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
 
import com.example.filmrental.dto.*;

import com.example.filmrental.repository.*;

import com.example.filmrental.model.*;
 
@Service

public class PaymentService {

	 @Autowired

	    private PaymentRepository paymentRepository;
 
	    private ModelMapper modelMapper = new ModelMapper();
 
	    public PaymentDTO addPayment(PaymentDTO paymentDTO) {

	        PaymentEntity payment = modelMapper.map(paymentDTO, PaymentEntity.class);

	        payment.setPaymentDate(paymentDTO.getPaymentDate());

	        payment.setLastUpdate(paymentDTO.getLastUpdate());

	        PaymentEntity savedPayment = paymentRepository.save(payment);

	        return modelMapper.map(savedPayment, PaymentDTO.class);

	    }

	    public Map<LocalDate, Double> getCumulativeRevenueDatewise() {

	        List<PaymentEntity> payments = paymentRepository.findAll();

	        Map<LocalDate, Double> revenueMap = new HashMap<>();

	        for (PaymentEntity payment : payments) {

	            LocalDate paymentDate = payment.getPaymentDate().toLocalDate();

	            revenueMap.put(paymentDate, revenueMap.getOrDefault(paymentDate, 0.0) + payment.getAmount());

	        }

	        return revenueMap;

	    }
 
	     

	    public Map<LocalDate, Double> getCumulativeRevenueByStoreDatewise(Long storeId) {

	        List<PaymentEntity> payments = paymentRepository.findAll();

	        Map<LocalDate, Double> revenueMap = new HashMap<>();

	        for (PaymentEntity payment : payments) {

	            if (payment.getRental().getInventory().getStore().getStoreId().equals(storeId)) {

	                LocalDate paymentDate = payment.getPaymentDate().toLocalDate();

	                revenueMap.put(paymentDate, revenueMap.getOrDefault(paymentDate, 0.0) + payment.getAmount());

	            }

	        }

	        return revenueMap;

	    }

	    public Map<String, Double> getCumulativeRevenueFilmwise() {

	        List<PaymentEntity> payments = paymentRepository.findAll();

	        Map<String, Double> filmRevenueMap = new HashMap<>();

	        for (PaymentEntity payment : payments) {

	            String filmTitle = payment.getRental().getInventory().getFilm().getTitle();

	            filmRevenueMap.put(filmTitle, filmRevenueMap.getOrDefault(filmTitle, 0.0) + payment.getAmount());

	        }

	        return filmRevenueMap;

	    }
 
	    public Map<String, Double> getCumulativeRevenueByFilmStorewise(Long filmId) {

	        List<PaymentEntity> payments = paymentRepository.findAll();

	        Map<String, Double> filmStoreRevenueMap = new HashMap<>();

	        for (PaymentEntity payment : payments) {

	            if (payment.getRental().getInventory().getFilm().getFilmId().equals(filmId)) {

	                String filmName = payment.getRental().getInventory().getFilm().getTitle();  // Get film name

	                String storeAddress = payment.getRental().getInventory().getStore().getAddress().getAddress();  // Get store address

	                String key = filmName + " - " + storeAddress;  // Create a composite key

	                filmStoreRevenueMap.put(key, filmStoreRevenueMap.getOrDefault(key, 0.0) + payment.getAmount());

	            }

	        }

	        return filmStoreRevenueMap;

	    }
 
 
//	    public Map<String, Double> getCumulativeRevenueByFilmStorewise(Long filmId) {

//	        List<Payment> payments = paymentRepository.findAll();

//	        Map<String, Double> storeRevenueMap = new HashMap<>();

//	        

//	        for (Payment payment : payments) {

//	            if (payment.getRental().getInventory().getFilm().getFilmId().equals(filmId)) {

//	                String storeAddress = payment.getRental().getInventory().getStore().getAddress().getAddress();

//	                storeRevenueMap.put(storeAddress, storeRevenueMap.getOrDefault(storeAddress, 0.0) + payment.getAmount());

//	            }

//	        }

//	        

//	        return storeRevenueMap;

//	    }

	    public Map<String, Double> getCumulativeRevenueFilmsByStore(Long storeId) {

	        List<PaymentEntity> payments = paymentRepository.findAll();

	        Map<String, Double> filmRevenueMap = new HashMap<>();

	        for (PaymentEntity payment : payments) {

	            if (payment.getRental().getInventory().getStore().getStoreId().equals(storeId)) {

	                String filmTitle = payment.getRental().getInventory().getFilm().getTitle();

	                filmRevenueMap.put(filmTitle, filmRevenueMap.getOrDefault(filmTitle, 0.0) + payment.getAmount());

	            }

	        }

	        return filmRevenueMap;

	    }
 


}

 
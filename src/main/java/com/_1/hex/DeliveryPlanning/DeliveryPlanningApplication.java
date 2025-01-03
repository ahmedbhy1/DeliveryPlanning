package com._1.hex.DeliveryPlanning;

import com._1.hex.DeliveryPlanning.application.Mvp;
import com._1.hex.DeliveryPlanning.view.MainDrawMap;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;


@SpringBootApplication
public class DeliveryPlanningApplication {

	public static void main(String[] args) {

		ApplicationContext context = SpringApplication.run(DeliveryPlanningApplication.class, args);

		Mvp mvp = context.getBean(Mvp.class);

		try {
			mvp.launch();
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
}

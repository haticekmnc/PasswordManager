/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.company.secrets.vault.security.authentication.service;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javax.faces.bean.ApplicationScoped;

/**
 *
 * @author hatice.kemence
 */
@ApplicationScoped
public class TimerService {
     private final ScheduledExecutorService scheduler;

    public TimerService() {
        scheduler = Executors.newScheduledThreadPool(1);
    }

   public void schedule(Runnable task, long delay) {
    scheduler.schedule(task, delay, TimeUnit.SECONDS);
    System.out.println("Zamanlayıcı tetiklendi.");
}


    public void shutdown() {
        if (scheduler != null) {
            scheduler.shutdown();
        }
    }
    
    
}

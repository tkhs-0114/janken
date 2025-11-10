package oit.is.z2911.kaizi.janken.service;

import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import oit.is.z2911.kaizi.janken.model.Match;
import oit.is.z2911.kaizi.janken.model.MatchMapper;

@Service
public class AsyncResult {
  int id = -1;

  @Autowired
  MatchMapper matchMapper;

  @Async
  public void getResult(SseEmitter emitter, int id) throws Exception {
    this.id = id;
    try {
      while (true) {
        Match match = matchMapper.selectById(this.id);
        if (match != null && !match.getIsActive()) {
          emitter.send(match);
          emitter.complete();
          break;
        } else {
          emitter.send("まだ相手が出していません");
        }
        TimeUnit.MILLISECONDS.sleep(500);
      }
    } catch (Exception e) {
      System.err.println(e.getMessage());
      emitter.completeWithError(e);
    }
  }
}

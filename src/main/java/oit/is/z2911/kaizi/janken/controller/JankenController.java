package oit.is.z2911.kaizi.janken.controller;

import oit.is.z2911.kaizi.janken.model.Entry;
import java.security.Principal;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import oit.is.z2911.kaizi.janken.model.User;
import oit.is.z2911.kaizi.janken.model.UserMapper;
import oit.is.z2911.kaizi.janken.model.Match;
import oit.is.z2911.kaizi.janken.model.MatchMapper;
import oit.is.z2911.kaizi.janken.model.MatchInfo;
import oit.is.z2911.kaizi.janken.model.MatchInfoMapper;

import oit.is.z2911.kaizi.janken.service.AsyncResult;

@Controller
public class JankenController {

  @Autowired
  private Entry entry;

  @Autowired
  UserMapper userMapper;
  @Autowired
  MatchMapper matchMapper;
  @Autowired
  MatchInfoMapper matchInfoMapper;
  @Autowired
  AsyncResult asyncResult;

  @GetMapping("/janken")
  public String janekn(ModelMap model) {

    ArrayList<User> users = userMapper.selectAll();
    model.addAttribute("users", users);

    ArrayList<Match> matches = matchMapper.selectByActive(false);
    model.addAttribute("matches", matches);

    ArrayList<Match> matchinfos = matchMapper.selectByActive(true);
    model.addAttribute("matchinfos", matchinfos);

    return "janken.html";
  }

  // @GetMapping("/jankengame")
  // public String sample23(@RequestParam String hand, ModelMap model) {
  // model.addAttribute("entry", this.entry);
  // String hands[] = { "Gu", "Ch", "Pa" };
  // String comHand = "Gu";
  // model.addAttribute("yourHand", hand);
  // model.addAttribute("comHand", comHand);
  // for (int i = 0; i < 3; i++) {
  // if (hand.equals(hands[i])) {
  // if (comHand.equals(hands[i])) {
  // model.addAttribute("result", "あいこ");
  // } else if (comHand.equals(hands[(i + 1) % 3])) {
  // model.addAttribute("result", "勝ち");
  // } else {
  // model.addAttribute("result", "負け");
  // }
  // }
  // }
  // return "janken.html";
  // }

  // @PostMapping("/janken")
  // public String roomIn(@RequestParam String name, ModelMap model) {
  // model.addAttribute("name", name);
  // return "janken.html";
  // }

  @GetMapping("/match")
  public String match(@RequestParam int id, ModelMap model) {
    model.addAttribute("user2Id", id);
    User user2 = userMapper.selectByUserId(id);
    model.addAttribute("user2", user2);
    return "match.html";
  }

  @GetMapping("/fight")
  public String fight(ModelMap model, @RequestParam int id, @RequestParam String hand, Principal principal) {
    User user1 = userMapper.selectByUserName(principal.getName());
    User user2 = userMapper.selectByUserId(id);
    model.addAttribute("user1", user1);

    // MatchInfo activeMatchInfo = matchInfoMapper.selectByUsers(user1.getId(),
    // user2.getId());
    Match activeMatch = matchMapper.selectByUsers(user1.getId(), user2.getId());
    if (activeMatch != null) {
      // すでに対戦情報がある場合
      // matchInfoMapper.deactivateMatchInfo(activeMatchInfo.getId());
      matchMapper.finMatch(activeMatch.getId(), hand);
      model.addAttribute("id", activeMatch.getId());
    } else {
      // 新しい対戦
      matchInfoMapper.insertMatchInfo(user1.getId(), user2.getId(), hand, true);
      Match newMatch = new Match();
      newMatch.setUser1(user1.getId());
      newMatch.setUser2(user2.getId());
      newMatch.setUser1Hand(hand);
      newMatch.setIsActive(true);
      matchMapper.insertMatch(newMatch);
      model.addAttribute("id", newMatch.getId());
    }
    return "wait.html";
  }

  @GetMapping("/result")
  public SseEmitter result(ModelMap model, @RequestParam int id, Principal principal) {
    final SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
    try {
      this.asyncResult.getResult(emitter, id);
    } catch (Exception e) {
      System.err.println(e.getMessage());
      emitter.complete();
    }
    return emitter;
  }
}

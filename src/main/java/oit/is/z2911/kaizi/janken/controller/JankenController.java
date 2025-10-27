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

import oit.is.z2911.kaizi.janken.model.User;
import oit.is.z2911.kaizi.janken.model.UserMapper;
import oit.is.z2911.kaizi.janken.model.Match;
import oit.is.z2911.kaizi.janken.model.MatchMapper;

@Controller
public class JankenController {

  @Autowired
  private Entry entry;

  @Autowired
  UserMapper userMapper;
  @Autowired
  MatchMapper matchMapper;

  @GetMapping("/janken")
  public String janekn(ModelMap model) {

    ArrayList<User> users = userMapper.selectAll();
    model.addAttribute("users", users);

    ArrayList<Match> matches = matchMapper.selectAll();
    model.addAttribute("matches", matches);

    return "janken.html";
  }

  @GetMapping("/jankengame")
  public String sample23(@RequestParam String hand, ModelMap model) {
    model.addAttribute("entry", this.entry);
    String hands[] = { "Gu", "Ch", "Pa" };
    String comHand = "Gu";
    model.addAttribute("yourHand", hand);
    model.addAttribute("comHand", comHand);
    for (int i = 0; i < 3; i++) {
      if (hand.equals(hands[i])) {
        if (comHand.equals(hands[i])) {
          model.addAttribute("result", "あいこ");
        } else if (comHand.equals(hands[(i + 1) % 3])) {
          model.addAttribute("result", "勝ち");
        } else {
          model.addAttribute("result", "負け");
        }
      }
    }
    return "janken.html";
  }

  @PostMapping("/janken")
  public String roomIn(@RequestParam String name, ModelMap model) {
    model.addAttribute("name", name);
    return "janken.html";
  }

  @GetMapping("/match")
  public String match(@RequestParam int id, ModelMap model) {
    User user2 = userMapper.selectByUserId(id);
    model.addAttribute("user2", user2);
    return "match.html";
  }

  @GetMapping("/matched")
  public String matched(@RequestParam String user1Name, @RequestParam int user2, @RequestParam String hand,
      ModelMap model) {
    int user1 = userMapper.selectByUserName(user1Name).getId();
    String user2Hand = "Gu";
    matchMapper.insertMatch(user1, user2, hand, user2Hand);
    return "match.html";
  }
}

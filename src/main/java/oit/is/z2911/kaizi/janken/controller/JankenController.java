package oit.is.z2911.kaizi.janken.controller;

import oit.is.z2911.kaizi.janken.model.Entry;
import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class JankenController {

  @Autowired
  private Entry entry;

  @GetMapping("/janken")
  public String janekn(Principal prin, ModelMap model) {
    String name = prin.getName();
    this.entry.addUser(name);
    model.addAttribute("entry", this.entry);
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
}

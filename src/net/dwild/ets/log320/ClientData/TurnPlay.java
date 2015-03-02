package net.dwild.ets.log320.ClientData;


public class TurnPlay {
 
 private Square from;
 private Square to;
 
 public TurnPlay(Square from, Square to) {
  this.from = from;
  this.to = to;
 }
 
 public Square getFrom() {
  return from;
 }
 
 public Square getTo() {
  return to;
 }
 
 public String toString() {
  return from.toString() + " - " + to.toString();
 }
}

(ns snake-clj.opengl.core
  (:import (java.util UUID))
  (:require [snake-clj.commands :as cmd]
            [play-clj.core :refer :all]
            [play-clj.ui :refer :all]))
(def game-id (atom 0))
(def seed 42)

(defscreen main-screen
  :on-show
  (fn [screen entities]
    (update! screen :renderer (stage))
    (label "Seed" seed))
  
  :on-render
  (fn [screen entities]
    (clear!)
    (render! screen entities)))

(defgame snake-clj-game
  :on-create
  (fn [this]
    (swap! game-id (fn [_] (UUID/randomUUID)))
    (cmd/start-game! @game-id seed)
    (set-screen! this main-screen)))

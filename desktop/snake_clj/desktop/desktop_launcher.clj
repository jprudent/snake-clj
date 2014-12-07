(ns snake-clj.desktop.desktop-launcher
  (:require [snake-clj.opengl.core :refer :all])
  (:import [com.badlogic.gdx.backends.lwjgl LwjglApplication]
           [org.lwjgl.input Keyboard])
  (:gen-class))

(defn -main
  []
  (LwjglApplication. snake-clj-game "play-clj-template" 800 600)
  (Keyboard/enableRepeatEvents true))

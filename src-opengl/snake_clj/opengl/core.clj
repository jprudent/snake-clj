(ns snake-clj.opengl.core
  (:import (java.util UUID)
           (com.badlogic.gdx.utils.viewport FitViewport))
  (:require [snake-clj.commands :as cmd]
            [snake-clj.matrix :as matrix]
            [snake-clj.core :as c]
            [snake-clj.db :as db]
            [play-clj.core :refer :all]
            [play-clj.ui :refer :all]
            [play-clj.g2d :refer :all]))

(declare snake-clj-game main-screen)
(def game-id 21)
(def seed 42)

(defn apple-texture [] (texture "apple.png"))
(def mem-apple-texture (memoize apple-texture))

(defn snake-texture [] (texture "snake.png"))
(def mem-snake-texture (memoize snake-texture))

(defn wall-texture [] (texture "wall.png"))
(def mem-wall-texture (memoize wall-texture))

(defn w->screen
  "Convert world position to screen position"
  [n]
  (* 4 n))

(defn apple-entities [world]
  (for [x (range (matrix/arity-x world))
        y (range (matrix/arity-y world))
        :when (c/apple? (matrix/get-at world [x y]))]
    (assoc (mem-apple-texture)
           :x (w->screen x)
           :y (w->screen y))))

(defn snake-entities [snake]
  (for [snake-bit snake
        :let [[snake-x snake-y] snake-bit]]
    (assoc (mem-snake-texture)
           :x (w->screen snake-x)
           :y (w->screen snake-y))))

(defn wall-entities [world]
  (for [x (range (matrix/arity-x world))
        y (range (matrix/arity-y world))
        :when (c/wall? (matrix/get-at world [x y]))]
    (assoc (mem-wall-texture)
           :x (w->screen x)
           :y (w->screen y))))

(defn update-entities []
  (let [{:keys [seed world snake]} (db/load-aggregate game-id)]
    (-> (into [] (apple-entities world))
        (into (snake-entities snake))
        (into (wall-entities world)))))

;; Original nokia is 84 x 48 pixels, but we will use virtual pixels of 4 pixels
(def w (* 84 4))
(def h (* 48 4))

(defn stage-fit-vp []
  (stage :set-viewport (FitViewport. w h)))

(defn init-graphic-settings [screen]
  (update! screen
           :camera (orthographic)
           :renderer (stage-fit-vp)))

(defn screen-wrapper []
  (set-screen-wrapper! (fn [screen screen-fn]
                         (try (screen-fn)
                              (catch Exception e
                                (.printStackTrace e)
                                (Thread/sleep 10000))))))

(defscreen main-screen
           :on-show
           (fn [screen _]
             (screen-wrapper)
             (init-graphic-settings screen)
             (add-timer! screen :go-ahead 1 1)
             (update-entities))

           :on-resize
           (fn [screen _]
             (size! screen w h))

           :on-render
           (fn [screen entities]
             (clear!)
             (render! screen entities)
             (update-entities))

           :on-timer
           (fn [screen entities]
             (print (str "timer ! " (:id screen)))
             (case (:id screen)
               :go-ahead (cmd/go-ahead! game-id)
               nil)
             entities))

(defgame snake-clj-game
         :on-create
         (fn [this]
           (cmd/start-game! game-id seed)
           (set-screen! this main-screen)))

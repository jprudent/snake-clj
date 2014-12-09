(ns snake-clj.commands
  (:require
    [snake-clj.core :refer :all]
    [snake-clj.events :as evt]
    [snake-clj.db :as db]))

(defn start-game! [id seed]
  (db/store! (evt/game-started id seed)))

(defn go-ahead! [id]
  (apply db/store!
         (if-let [{:keys [alive?]} (db/load-aggregate id)]
           (if alive?
             (let [turned-evt (evt/gone-ahead id)
                   snake-ahead-state (db/load-aggregate id turned-evt)
                   hit-smthing-evt (cond
                                     (wall? (what-is-on-head snake-ahead-state)) (evt/hit-wall id)
                                     (apple? (what-is-on-head snake-ahead-state)) (evt/ate-apple id)
                                     (tail? (what-is-on-head snake-ahead-state)) (evt/ate-tail id))]
               (if (nil? hit-smthing-evt)
                 [turned-evt]
                 [turned-evt hit-smthing-evt])))
           [])))

(defn turn-left! [id]
  (if (:alive? (db/load-aggregate id))
    (do
      (apply db/store! [(evt/turned-left id)])
      (go-ahead! id))))

(defn turn-right! [id]
  (if (:alive? (db/load-aggregate id))
    (do
      (apply db/store! [(evt/turned-right id)])
      (go-ahead! id))))
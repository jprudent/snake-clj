snake-clj
=========

An event sourced snake game written in clojure for Ludum Dare #31

REPL
====

Launch the game :

        (-main)

Handle runtime exceptions :

        (in-ns 'snake-clj.opengl.core)
        (set-screen-wrapper! (fn [screen screen-fn]
                               (try (screen-fn)
                                 (catch Exception e
                                   (.printStackTrace e)
                                   ))))



Licence
=======

Public Domain

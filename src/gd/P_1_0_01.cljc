(ns gd.P-1-0-01
  (:require [quil.core :as q]
            [quil.middleware :as m]))

;DESCRIPTION: Two rectangles are drawn. The color of the first rectangle is determined by the vertical position of the mouse. The color of the second rectangle is determined by the horizontal position of the mouse. The size of the rectangles is determined by the mouse position.

(defn setup []
  (q/frame-rate 30)
  (q/no-cursor)
  (q/color-mode :hsb 360 100 100)
  (q/rect-mode :center)
  (q/no-stroke))

(def sketch (atom nil))


(def width 400)
(def half-width (/ width 2))

(defn draw-state [state]
  (q/background (/ (q/mouse-y) 2) 100 100)
  (q/fill (- half-width (/ (q/mouse-x) 2)) 100 100)
  (q/rect half-width half-width (inc (q/mouse-x)) (inc (q/mouse-y))))

(defn key-pressed [state {:keys [key key-code]}]
  (when (or (= key :s) (= key :S))
    (q/save "/Users/andriylevitskyy/Desktop/generative_art/P_1_0_01.jpg")))


(defn run-sketch []
  (q/defsketch sketch
    :host "sketch-canvas"
    :title "P_1_0_01"
    :size [width width]
    :setup setup
    :draw draw-state
    :middleware [m/fun-mode]
    :key-pressed key-pressed))

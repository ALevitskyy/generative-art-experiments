(ns gd.P-2-0-02
  (:require [quil.core :as q]
            [quil.middleware :as m]))

(def width 720)
(def TAU (* 2 Math/PI))

(defn setup []
  (q/no-fill)
  (q/background 255)
  (q/stroke-weight 2)
  (q/stroke 0 25))

(defn draw-shape []
  (q/push-matrix)
  (q/translate (/ width 2) (/ width 2))
  (q/begin-shape)
  (let [circle-resolution (int (q/map-range (+ (q/mouse-y) 100) 0 width 2 10))
        radius (- (q/mouse-x) (/ width 2))
        angle (/ TAU circle-resolution)]
    (doseq [i (range (+ circle-resolution 1))]
      (q/vertex
       (* radius (Math/cos (* i angle)))
       (* radius (Math/sin (* i angle))))))
  (q/end-shape)
  (q/pop-matrix))

(defn draw-state [state]
  (when (and (q/mouse-pressed?) (= (q/mouse-button) :left))
    (draw-shape)))


(defn key-pressed [state {:keys [key key-code]}]
  (when (or (= key :s) (= key :S))
    (q/save "/Users/andriylevitskyy/Desktop/generative_art/P_2_0_02.jpg")))

(def sketch (atom nil))
(q/defsketch sketch
  :host "sketch-canvas"
  :title "P_2_0_02"
  :size [width width]
  :setup setup
  :draw draw-state
  :middleware [m/fun-mode]
  :key-pressed key-pressed)
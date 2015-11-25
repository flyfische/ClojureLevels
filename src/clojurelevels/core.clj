(ns clojurelevels.core
  (:gen-class)
(:require [clojure.data.json :as json])
  )

(defn getdata [sitenum] 
	(json/read-str 
	(slurp (str "http://waterservices.usgs.gov/nwis/iv/?format=json&sites=" sitenum))))
(defn getjsonroot [data]
	(get (get data "value") "timeSeries"))
(defn parsedata [data]
	(let 
		[varName (get (get data "variable") "variableName") 
		varVal (get (get (get (get (get data "values") 0) "value") 0) "value")]
	(hash-map :variable varName :value varVal))
	)

(defn getsite [site]
	(map parsedata (getjsonroot (getdata site))))

(defn prettyprint
  [{variable :variable value :value}]
  (println (str variable ": " value))

  )
(defn -main
  [& args]
 	(dorun (map prettyprint (getsite (nth args 0))))
  )

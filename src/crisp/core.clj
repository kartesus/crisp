(ns crisp.core)

(def self-evaling
  (some-fn number? string? nil?))

(defn crisp-eval [env expr]
  (cond
    (self-evaling? expr)
    expr

    (symbol? expr)
    (if (contains? env expr)
      (get env expr)
      (throw (ex-info "Unbound symbol" {:symbol expr :env env})))

    (seq? expr)
    (let [[op & args] expr]
      (case op

        if (let [[test then else] args]
             (if (crisp-eval env test)
               (crisp-eval env then)
               (crisp-eval env else)))

        quote (first args)

        do (last (map #(crisp-eval env %) args))

        let (let [[bindings & body] args
                  env' (into env (for [[k v] (partition 2 bindings)]
                                   [k (crisp-eval env v)]))]
              (crisp-eval env' (cons 'do body)))))))

(define member?
  (lambda (item lst)
    (cond
      ((null? lst) #f)
      ((equal? item (car lst)) #t)
      (else (member? item (cdr lst)))
)))

(member? 3 '(1 2 3)) ;#t
(member? 3 '(1 (3) 2)) ;#f
(member? 'b '(a (b) d)) ;#f
(member? 'b '(a b d)) ;#t
(member? 'b '(a (b c) d)) ;#f

(define remove-last
  (lambda (item lst)
    (cond 
      ((null? lst) '())
      ((and (equal? item (car lst)) (not (member? item (cdr lst)))) (cdr lst))
      (else (cons (car lst) (remove-last item (cdr lst)))))
))

(remove-last 'a '(a b a)) 
(remove-last 'a '(a b c))  ;-> (b c)
(remove-last 'a '(b a n a n a s)) ;-> (b a n a n s)
(remove-last '(a b) '(a b  (b a))) ;->(a b (b a))
(remove-last '(a b) '(a b  (a b))) ;->(a b)
(remove-last '(a b) '((a b) a b (b a) a b (a b) a b)) ;->((a b) a b (b a) a b a b)
(remove-last '(a b) '(a b (a b) a b (b a) a b (a b) a b)) ;    -> (a b (a b) a b (b a) a b a b)



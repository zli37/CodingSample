function [r,c,s,nfe,nge,nhe,f,gr,gc,hr,hc,delta,betaDelta,...
    d] ...
    = activeset(r,c,alpha,A,xvals,yvals)

%
% nfe is the # of function evaluations
% nge is the # of gradient evaluations
% nhe is the # of Hessian evaluations
%
% delta is the step for r
% d is the step for c
nfe = 0;
nge = 0;
nhe = 0;
smallcorrection(1:19)=0.5;

% It takes very long time for it to finish
% In order to check the efficiency,
% I only take 100 steps.
n=0; 
while n<100 % do the loop forever until the condition is satisfied
    n=n+1;  % # of steps to run...
    s=sign(c);
    [f,gr,gc,hr,hc] = likelihood(r,c,xvals,yvals);
    nfe = nfe + 1;
    nge = nge + 1;
    nhe = nhe + 1;
    
    %Newton step
    %check if matrices are singular
    if hr < 1e-2
        hr = hr + 0.5;
    end
    
    if det(hc) < 1e-2
        hc = hc + diag(smallcorrection);
    end
    delta = - inv(hr)*gr;
    d = - inv(hc)*(gc+alpha*s');
    betaDelta = 1; % Step length for delta
    % Armijo/Backtracking for delta
    while likelihood(r+betaDelta*delta,c,xvals,yvals)...
            > f + A*betaDelta*gr*delta
        nfe = nfe + 1;
        betaDelta = betaDelta/2;
    end
    % d is a colunm vector
    % beta is the step for c
    
     beta = min(1,min(-c./d'));
    
    % Armijo/Backtracking for d
    while likelihood(r,c+beta*d',xvals,yvals)...
            +alpha*beta*d'*s'>...
            f + A*beta*d'*(gc'+alpha*s)'
        nfe = nfe + 1;
        beta = beta/2;
    end
    % Take the step
    r = r + betaDelta*delta;
    c = c + beta*d';

    for i = 1:19
        if abs(c(i)+beta*d(i))< 1e-10 && c(i)~=0
            s(i)=0;
        end
    end
    
    [f,gr,gc,hr,hc] = likelihood(r,c,xvals,yvals);
    nfe = nfe + 1;
    nge = nge + 1;
    nhe = nhe + 1;
    
    % Update 
    % The amount of c to change is 0.01
    for i = 1:19
        if c(i) == 0 && gc(i) > alpha
            c(i) = -1e-6;
            s(i) = -1;
        elseif c(i) == 0 && gc(i) < -alpha
            c(i) = +1e-06;
            s(i) = +1;
        end
    end
    
    % Gradient step
     delta = -gr;
     betaDelta = 1;
      while likelihood(r+betaDelta*delta,c,xvals,yvals)...
              > likelihood(r,c,xvals,yvals) + A*betaDelta*gr*delta
          nfe = nfe + 1;
          betaDelta = betaDelta/2;
      end
    
    for i = 1:19
        if c(i) ~=0 || abs(gc(i)) > alpha
            d(i) = -gc(i) - alpha * s(i);
        else
            d(i) = 0;
        end
    end
    
    beta = min(1,min(-c./d'));
     while likelihood(r,c+beta*d',xvals,yvals)...
             + alpha*beta*d'*s' > ...
             likelihood(r,c,xvals,yvals)...
             + A*beta*d'*(gc'+alpha*s)'
         nfe = nfe + 1;
         beta = beta/2;
     end
    
    % Take the gradient step
    r = r + betaDelta*delta;
    c = c + beta*d';
    
    % KKT condition
    KKT = 0;
    [f,gr,gc,hr,hc] = likelihood(r,c,xvals,yvals);
    nfe = nfe + 1;
    nge = nge + 1;
    nhe = nhe + 1;
    
    if abs(gr) > 1e-6
         KKT = KKT + 1;
    end
         for i = 1:19
             if abs(gc(i) + alpha) > 1e-6 || abs(gc(i) - alpha) > 1e-6 
               KKT = KKT + 1;
             end
         end
      
     if KKT == 0
          return
     end
end
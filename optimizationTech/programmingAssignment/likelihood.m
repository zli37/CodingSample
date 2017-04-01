function [f,gr,gc,Hr,Hc] = likelihood(r,c,xvals,yvals)
%Log likelihood function returns
%function value, gradient and Hessian

%Evaluate the function
y=zeros(200,1);
f=0;
for i=1:200
y(i)=yvals(i)*log(1+exp(r+xvals(i,:)*c'))+(1-yvals(i))...
    *log(1+exp(-r-xvals(i,:)*c'));
f=f+y(i);
end
%
% Evaluate the gradient with respect to r
if nargout > 1
%     dgr=zeros(200,1);
    gr=0;
    for i=1:200
        dgr=yvals(i)-1/(exp(r+xvals(i,:)*c')+1);
    gr=gr+dgr;
    end
end

%
% Evaluate the gradient with respect to c
if nargout > 2
    gc=zeros(19,1);
    for j=1:19
        gctemp=zeros(200,1);
        for i=1:200
            gctemp(i)=xvals(i,j)*(yvals(i)-1/(exp(r+xvals(i,:)*c')+1));
        gc(j)=gc(j)+gctemp(i);
        end
    end
end

%
% Evaluate the Hessian matrix with respect to r

if nargout > 3
    ddHr=zeros(200,1);
    Hr=0;
    for i=1:200
        ddHr(i)=exp(r+xvals(i,:)*c')/(1+exp(-r-xvals(i,:)*c'))^2;
    Hr=Hr+ddHr(i);
    end
end

%
% Evaluate the Hessian matrix with respect to c

if nargout > 4
    Hc=zeros(19,19);
    for j=1:19
        for k=1:19 
            Hctemp=zeros(200,1);
            for i=1:200
                Hctemp(i)=xvals(i,j)*xvals(i,k)...
                    *exp(r+xvals(i,:)*c')/(1+exp(-r-xvals(i,:)*c'))^2;
            Hc(j,k)=Hc(j,k)+Hctemp(i);
            end
        end
    end
end

    
    
    

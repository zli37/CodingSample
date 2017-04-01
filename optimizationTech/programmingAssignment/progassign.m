% Script to read ICU data file
% Output in data (rows are data items, columns are features
% as listed in headers)
clear;clc;
lines_to_skip = 12;
collist = [6,8
11,11
14,15
18,18
21,21
24,24
27,27
30,30
33,33
36,36
39,41
44,46
49,49
52,52
55,55
58,58
61,61
64,64
67,67
70,70
73,73];
data = read_dat_file('icu.dat',collist,lines_to_skip);
headers = {'Identification';
'Vital Status';
'Age';
'Gender';
'Race';
'Service at ICU admission';
'Cancer part of problem';
'History of Renal Failure';
'Infection probable at ICU admission';
'CPR prior to ICU admission';
'Systolic blood pressure at ICU admission';
'Heart rate at ICU admission';
'Previous admission to an ICU within 6 months';
'Type of admission';
'Long bone, multiple, neck, single area or hip fracture';
'PO2 from initial blood gases';
'PH from initial blood gases';
'PCO2 from initial blood gases';
'Bicarbonate from initial blood gases';
'Creatinine from initial blood gases';
'Level of consciousness at ICU admission'}
% r and c are the initial guess
r = 0
c = zeros(1,19)
% alpha is another important parameter for efficiency 
% of the algorithm
alpha = 0.05
% A is the Armijo/Backtracking constant
A=10^(-4);
yvals = data(:,2)
xvals = data(:,3:end)
  [r,c,s,nfe,nge,nhe,f,gr,gc,hr,hc,delta,betaDelta...
     d] ...
     = activeset(r,c,alpha,A,xvals,yvals)



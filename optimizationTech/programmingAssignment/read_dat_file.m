function X = read_dat_file(filename,collist,lines_to_skip)
% function X = read_dat_file(filename,collist,lines_to_skip)
%
% Reads the file named filename skipping the first 
% lines_to_skip many lines. The remainder of the file should
% consist of lines where the j'th feature is in columns
% collist(j,1) to collist(j,2).
% Then X(i,:) is the list of values for the i'th line
% after the skipped lines.
fid = fopen(filename,'r');
if fid < 0
    fprintf('read_dat_file: Cannnot open %s\n',filename)
    X = [];
    return
end

for i = 1:lines_to_skip
    line = fgetl(fid);
end
i = 0;
X = zeros(0,size(collist,1));
linedata = zeros(1,size(collist,1));
maxcol = max(collist(:,2));
while ~ feof(fid)
    line = fgetl(fid);
    if length(line) < maxcol
        continue
    end
    for j = 1:size(collist,1)
        linedata(j) = sscanf(line(collist(j,1):collist(j,2)),'%g');
    end
    X = [X; linedata];
end

fclose(fid);
#/bin/bash

for DIR in `find . -maxdepth 1 -type d`
do
	echo ""
	DIR=`echo $DIR | sed "s/\.\///g"`
	if [ -d "$DIR/.git" ]; then
		echo  "working on $DIR"
		cd $DIR
		echo  "git $*"
		git $*
		cd ..
	fi
done


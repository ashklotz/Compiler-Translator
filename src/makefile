JFLAGS = -g
JC = javac
.SUFFIXES: .java .class
.java.class:
        $(JC) $(JFLAGS) $*.java

CLASSES: \
		VarGenerator.java \
		Token.java \
		compfs.java \
		Scanner.java \
		Parser.java \
		Node.java

default: classes

classes: $(CLASSES:.java=.class)

clean:
	$(RM) *.class
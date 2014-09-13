The POSTaggedFile.java is adapted from the previous homework to provide data into the acceptable format for mallet.
The file has been commented at specific sections for the various types of orthogrpahic features. Uncomment them to add the feature tags to the token.
The backward model has also been commented out. Comment the forward model and uncomment it to get tags for sentences in reverse order. 
Run the POSTaggedFile.java
javac -Xms64M -Xmx64M POSTaggedFile.java 
java -Xms64M -Xmx64M POSTaggedFile pos/atis>atis

The job subsmission script for TACC for HMM and CRF training has also been provided. Edit them for the appropriate corpus.
Use the command 
sbatch CRFtrainjob
to submit the job
You can use scancel to cancel the job.

The edited TokenAccuracyEvaluator.java has been added for calculating OOV accuracy. Replace it into the src/cc/mallet/fst 
directory and run ant to get it built. Once done, we are set to calculate OOV accuracy. 

Some sample output typescripts are provided in the output directory for the different experiments run. 
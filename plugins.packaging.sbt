// This ensures that after the `dist` task creates the distribution file (`project-version.zip`)
// we have a copy of the file named `current.zip`.
// Both these files are to be uploaded to our S3 artifact repository to facilitate deployment.

packageBin in Universal := {
  val originalFileName = (packageBin in Universal).value
  val newFileName = file(originalFileName.getParent) / "current.zip"
  IO.copyFile(originalFileName, newFileName)
  originalFileName
}

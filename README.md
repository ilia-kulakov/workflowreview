Workflow Review: AEM 6.1
========


Create a Workflow

Create a workflow that lets administrators review newly created pages and move them under the proper parent.

The workflow is launched every time a page is created under /content/geometrixx. The new page goes to the workflow as
the payload.

The workflow checks pathToMove property of the page’s jcr:content node. If the property is not present, the workflow is
assigned to administrators group. A group member enters the pathToMove property using a dialog.

After that, the workflow validates pathToMove property. If it contains a valid path, it moves the page to that path. If
the path is empty, invalid, or matches the current path, no action is taken.



This a content package project generated using the multimodule-content-package-archetype.

Building
--------

This project uses Maven for building. Common commands:

From the root directory, run ``mvn -PautoInstallPackage clean install`` to build the bundle and content package and install to a CQ instance.

From the bundle directory, run ``mvn -PautoInstallBundle clean install`` to build *just* the bundle and install to a CQ instance.

Using with VLT
--------------

To use vlt with this project, first build and install the package to your local CQ instance as described above. Then cd to `content/src/main/content/jcr_root` and run

    vlt --credentials admin:admin checkout -f ../META-INF/vault/filter.xml --force http://localhost:4502/crx

Once the working copy is created, you can use the normal ``vlt up`` and ``vlt ci`` commands.

Specifying CRX Host/Port
------------------------

The CRX host and port can be specified on the command line with:
mvn -Dcrx.host=otherhost -Dcrx.port=5502 <goals>


